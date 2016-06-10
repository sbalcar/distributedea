package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitialization;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescriptionwrapper.MethodDescriptionsWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class PlannerMethodDescription implements Planner {

	private PlannerInitialization plannerInit = null;
	
	private MethodDescriptionsWrapper knownMethods;
	
	private Random ran = new Random();
	
	
	public PlannerMethodDescription() {} // for serialization
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws PlannerException {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");

		// get all available Agent Description
		PlannerInitializationState state = PlannerInitializationState.RUN_ALL_COMBINATIONS;
		
		PlannerInitialization planner = new PlannerInitialization(state, true);
		planner.agentInitializationOnlyCreateAgents(centralManager, job, logger);
		
		knownMethods = ComputingAgentService.sendGetMethodDescriptions(centralManager, logger);
		planner.exit(centralManager, logger);
		
		
		// initialize one agent per core
		PlannerInitializationState stateInit = PlannerInitializationState.RUN_ONE_AGENT_PER_CORE;
		
		MethodDescriptionsWrapper exploitationMethodDescriptionsWrapper =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<AgentConfiguration> exploitationAgentConfigurations =
				exploitationMethodDescriptionsWrapper.exportAgentConfigurations();
		AgentConfigurations agentConfigurations =
				new AgentConfigurations(exploitationAgentConfigurations);
		
		JobRun exploitationJobRun = new JobRun(job);
		exploitationJobRun.setAgentConfigurations(agentConfigurations);
		
		plannerInit = new PlannerInitialization(stateInit, true);
		plannerInit.agentInitialization(centralManager, exploitationJobRun, logger);
	}

	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			 Iteration iteration, ReceivedData receivedData, AgentLogger logger
			 ) throws PlannerException {
		
		plannerInit.replan(centralManager, job, iteration, receivedData, logger);

		// get ratio of exploration and exploatation
		ResultOfComputingWrapper resultOfComputingWrapper =
				receivedData.getResultOfComputingWrapper();
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return;
		}
		
		AgentDescription candidate = plannerInit.removeNextCandidate();
		if (candidate != null) {
			// remove worst and replace by new candidate
			logger.log(Level.INFO, "Trying next candidate");
			replaceWorstByCandidate(centralManager, job,
					resultOfComputingWrapper, candidate, logger);
			return;
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
			replaceWorstExploitationMethods(centralManager, job,
					resultOfComputingWrapper, logger);
		}
	}
	
	private void replaceWorstByCandidate(Agent_CentralManager centralManager, JobRun job,
			ResultOfComputingWrapper resultOfComputingWrapper, AgentDescription candidate,
			AgentLogger logger) throws PlannerException {
		
		Problem problem = job.getProblem();
		
		ResultOfComputing worstResultOfComputing =
				resultOfComputingWrapper.exportWorstResultOfComputing(problem);
		AgentConfiguration worstAgentConfiguration =
				worstResultOfComputing.getAgentDescription().getAgentConfiguration();
		AID aidToKill = worstAgentConfiguration.exportAgentAID();
		
		AgentConfiguration newAgentConfiguration = candidate.getAgentConfiguration();
		Class<?> newProblemToolClass = candidate.exportProblemToolClass();
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
		
		PlannerTool.killAndCreateAgent(centralManager, aidToKill,
				newAgentConfiguration, problemStruct, logger);
	}
	
	public void replaceWorstExploitationMethods(Agent_CentralManager centralManager,
			JobRun job, ResultOfComputingWrapper resultOfComputingWrapper,
			AgentLogger logger) throws PlannerException {
			
		Problem problem = job.getProblem();
		List<Class<?>> problemTools = job.getProblemTools().getProblemTools();
		
		
		
		MethodDescriptionsWrapper availableExploitationMethods =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<Class<?>> availableExploitationAgentTypes =
				availableExploitationMethods.exportAgentTypes();
		// choosing agent exploitation Configuration to kill		
		ResultOfComputingWrapper exploitationResults =
				resultOfComputingWrapper.exportResultsOfGivenAgentType(availableExploitationAgentTypes);
		ResultOfComputing worst =
				exploitationResults.exportWorstResultOfComputing(problem);
		//if system don't contain any exploitation method
		if (worst == null) {
			worst = resultOfComputingWrapper.exportWorstResultOfComputing(problem);
		}
		
		AID aidToKill = worst.exportAgentConfiguration().exportAgentAID();
		
		

		// choose method description to create new agent
		MethodDescriptionsWrapper availableExplorationMethods =
				knownMethods.exportExplorationMethodDescriptions();
		List<Class<?>> availableExplorationAgentTypes =
				availableExplorationMethods.exportAgentTypes();
		
		List<Class<?>> notRunningExplorationAgentTypes = resultOfComputingWrapper.
				exportAgentTypesWhichDontContains(availableExplorationAgentTypes);
		
		AgentConfiguration newAgentConfiguration;
		Class<?> newProblemToolClass;
		
		// if exists some exploration type of agent which is not running -> start random of that 
		if (! notRunningExplorationAgentTypes.isEmpty()) {
			
			int agentTypeIndex = ran.nextInt(notRunningExplorationAgentTypes.size());
			int problemToolIndex = ran.nextInt(problemTools.size());
			
			Class<?> selectedAgentType = notRunningExplorationAgentTypes.get(agentTypeIndex);
			Class<?> selectedProblemTool = problemTools.get(problemToolIndex);
			
			newAgentConfiguration = new AgentConfiguration(selectedAgentType, null);
			newProblemToolClass = selectedProblemTool;
			
		} else {
			
			// duplicate the best exploration agent
			ResultOfComputingWrapper explorationResultOfComputingWrp =
					resultOfComputingWrapper.exportResultsOfGivenAgentType(availableExplorationAgentTypes);
			ResultOfComputing bestExplorationResultOfComputingWrp =
					explorationResultOfComputingWrp.exportBestResultOfComputing(problem);
			
			AgentDescription agentDescription =
					bestExplorationResultOfComputingWrp.getAgentDescription();
			
			newAgentConfiguration = agentDescription.getAgentConfiguration();
			newProblemToolClass = agentDescription.exportProblemToolClass();
		}
		
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
		
		PlannerTool.killAndCreateAgent(centralManager, aidToKill,
				newAgentConfiguration, problemStruct, logger);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
