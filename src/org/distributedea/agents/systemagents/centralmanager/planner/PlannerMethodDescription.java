package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel.ResultsOfComputing;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescriptionwrapper.MethodDescriptionsWrapper;
import org.distributedea.ontology.problem.Problem;

public class PlannerMethodDescription implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	private MethodDescriptionsWrapper knownMethods;
	
	private Random ran = new Random();
	
	public PlannerMethodDescription() {} // for serialization
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws PlannerException {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");

		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		// get all available Agent Description
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ALL_COMBINATIONS;
		
		PlannerInitialisation planner = new PlannerInitialisation(state, true);
		planner.agentInitialisationOnlyCreateAgents(centralManager, jobRun, logger);
		
		knownMethods = ComputingAgentService.sendGetMethodDescriptions(centralManager, logger);
		planner.exit(centralManager, logger);
		
		
		// initialize one agent per core
		PlannerInitialisationState stateInit = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		
		MethodDescriptionsWrapper exploitationMethodDescriptionsWrapper =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<AgentConfiguration> exploitationAgentConfigurations =
				exploitationMethodDescriptionsWrapper.exportAgentConfigurations();
		AgentConfigurations agentConfigurations =
				new AgentConfigurations(exploitationAgentConfigurations);
		
		JobRun exploitationJobRun = new JobRun(jobRun);
		exploitationJobRun.setAgentConfigurations(agentConfigurations);
		
		plannerInit = new PlannerInitialisation(stateInit, true);
		return plannerInit.agentInitialisation(centralManager, iteration,
				exploitationJobRun, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws PlannerException {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		RePlan rePlan = replanning(iteration, history);
		PlannerTool.processReplanning(centralManager, rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlan);
	}
	
	private RePlan replanning(Iteration iteration, History history
			 ) throws PlannerException {

		// get ratio of exploration and exploatation
		ResultsOfComputing resultOfComputingWrapper =
				ComputingAgentService.sendAccessesResult_(centralManager, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return new RePlan(iteration);
		}
		
		AgentDescription candidate = plannerInit.removeNextCandidate();
		if (candidate != null) {
			// remove worst and replace by new candidate
			logger.log(Level.INFO, "Trying next candidate");
			return replaceWorstByCandidate(jobRun,	iteration,
					resultOfComputingWrapper, candidate);
		}
		
		MethodDescriptionsWrapper exploitationMethodDescriptions =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<Class<?>> exploitationAgentTypes = 
				exploitationMethodDescriptions.exportAgentTypes();
		
		int numOfAllAgent = resultOfComputingWrapper.getResultOfComputing().size();
		int numOfExploitationAgentTypes = resultOfComputingWrapper
				.exportAgentNumberOfType(exploitationAgentTypes);
		
		int numOfExplorationAgentTypes = numOfAllAgent - numOfExploitationAgentTypes;
		
		double ratioExplorationDivSize = (double)numOfExplorationAgentTypes / (double) numOfAllAgent;
		logger.log(Level.INFO, "Exploitation / Exploration = " +
				numOfExploitationAgentTypes + " / " + numOfExplorationAgentTypes);
		
		double ratioIteration = iteration.exportRationOfIteration();

		if (ratioExplorationDivSize + 0.1 < ratioIteration) {

			logger.log(Level.INFO, "Less Exploitation, more Exploration methods");
			return replaceWorstExploitationMethods(jobRun, iteration,
					resultOfComputingWrapper);
		}
		
		return new RePlan(iteration);
	}
	
	private RePlan replaceWorstByCandidate(
			JobRun job, Iteration iteration, ResultsOfComputing resultOfComputingWrapper,
			AgentDescription candidateMethod) throws PlannerException {
		
		Problem problem = job.getProblem();
		
		IndividualWrapper worstResultOfComputing =
				resultOfComputingWrapper.exportWorstResultOfComputing(problem);
		AgentDescription methodToKill =
				worstResultOfComputing.getAgentDescription();
						
		return new RePlan(iteration, methodToKill, candidateMethod);
	}
	
	public RePlan replaceWorstExploitationMethods(JobRun job,
			Iteration iteration, ResultsOfComputing resultOfComputingWrapper
			) throws PlannerException {
			
		Problem problem = job.getProblem();
		List<Class<?>> problemTools = job.getProblemTools().getProblemTools();
		
		
		
		MethodDescriptionsWrapper availableExploitationMethods =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<Class<?>> availableExploitationAgentTypes =
				availableExploitationMethods.exportAgentTypes();
		
		// choosing agent exploitation Configuration to kill		
		ResultsOfComputing exploitationResults =
				resultOfComputingWrapper.exportResultsOfGivenAgentType(availableExploitationAgentTypes);
		IndividualWrapper worst =
				exploitationResults.exportWorstResultOfComputing(problem);
		//if system don't contain any exploitation method
		if (worst == null) {
			worst = resultOfComputingWrapper.exportWorstResultOfComputing(problem);
		}
		
		AgentDescription methodToKill = worst.getAgentDescription();
		
		

		// choose method description to create new agent
		MethodDescriptionsWrapper availableExplorationMethods =
				knownMethods.exportExplorationMethodDescriptions();
		List<Class<?>> availableExplorationAgentTypes =
				availableExplorationMethods.exportAgentTypes();
		
		List<Class<?>> notRunningExplorationAgentTypes = resultOfComputingWrapper.
				exportAgentTypesWhichDontContains(availableExplorationAgentTypes);
		
		AgentDescription newMethod;
		
		// if exists some exploration type of agent which is not running -> start random of that 
		if (! notRunningExplorationAgentTypes.isEmpty()) {
			
			int agentTypeIndex = ran.nextInt(notRunningExplorationAgentTypes.size());
			int problemToolIndex = ran.nextInt(problemTools.size());
			
			Class<?> selectedAgentType = notRunningExplorationAgentTypes.get(agentTypeIndex);
			Class<?> selectedProblemTool = problemTools.get(problemToolIndex);
			
			AgentConfiguration newAgentConfiguration = new AgentConfiguration(selectedAgentType, null);
			Class<?> newProblemToolClass = selectedProblemTool;
			
			newMethod = new AgentDescription();
			newMethod.setAgentConfiguration(newAgentConfiguration);
			newMethod.importProblemToolClass(newProblemToolClass);
		} else {
			
			// duplicate the best exploration agent
			ResultsOfComputing explorationResultOfComputingWrp =
					resultOfComputingWrapper.exportResultsOfGivenAgentType(availableExplorationAgentTypes);
			IndividualWrapper bestExplorationResultOfComputingWrp =
					explorationResultOfComputingWrp.exportBestResultOfComputing(problem);
			
			newMethod =
					bestExplorationResultOfComputingWrp.getAgentDescription();
		}
		
		return new RePlan(iteration, methodToKill, newMethod);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
