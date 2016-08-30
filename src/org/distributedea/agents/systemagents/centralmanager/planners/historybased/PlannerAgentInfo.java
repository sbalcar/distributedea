package org.distributedea.agents.systemagents.centralmanager.planners.historybased;

import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.agentinfo.AgentInfosWrapper;
import org.distributedea.ontology.configuration.AgentConfigurations;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfigurations;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;


public class PlannerAgentInfo implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	private AgentInfosWrapper knownMethods;
	
	
	public PlannerAgentInfo() {} // for serialization
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");

		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		// get all available Agent Description
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ALL_COMBINATIONS;
		
		PlannerInitialisation planner = new PlannerInitialisation(state, true);
		planner.agentInitialisationOnlyCreateAgents(centralManager, iteration,
				jobRun, logger);
		
		knownMethods = ComputingAgentService.getAgentInfos(centralManager, logger);
		planner.exit(centralManager, logger);
		
		
		// initialize one agent per core
		PlannerInitialisationState stateInit = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		
		AgentInfosWrapper exploitationMethodDescriptionsWrapper =
				knownMethods.exportExploitationAgentInfos();
		AgentConfigurations exploitationAgentConfigurations =
				exploitationMethodDescriptionsWrapper.exportAgentConfigurations();
		InputAgentConfigurations agentConfigurations =
				exploitationAgentConfigurations.exportInputAgentConfigurations();
		
		JobRun exploitationJobRun = new JobRun(jobRun);
		exploitationJobRun.setAgentConfigurations(agentConfigurations);
		
		plannerInit = new PlannerInitialisation(stateInit, true);
		return plannerInit.agentInitialisation(centralManager, iteration,
				exploitationJobRun, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
			 ) throws Exception {

		History historyOfCurrentMethods = history.
				exportHistoryOfRunningMethods(iteration, 3);
		
		if (historyOfCurrentMethods.getNumberOfMethodInstances() <= 1) {
			return new InputRePlan(iteration);
		}
		
		MethodsStatistics currentMethodsResults =
				historyOfCurrentMethods.exportMethodsResults(iteration);
		
		
		InputAgentDescription candidate = plannerInit.removeNextCandidate();
		if (candidate != null) {
			// remove worst and replace by new candidate
			logger.log(Level.INFO, "Trying next candidate");
			
			MethodStatistic methodStatistic = currentMethodsResults.
					exportMethodAchievedTheLeastQuantityOfImprovement();
			AgentDescription methodToKill = methodStatistic.
					exportAgentDescriptionClone();

			return new InputRePlan(iteration, methodToKill, candidate);
		}
		

		AgentInfosWrapper exploitationMethodDescriptions =
				knownMethods.exportExploitationAgentInfos();
		List<Class<?>> exploitationAgentTypes = 
				exploitationMethodDescriptions.exportAgentTypes();
		
		MethodsStatistics resultOfExploitationMethods = currentMethodsResults.
				getMethodResultsContainsInstancesOf(exploitationAgentTypes);
		
		
		AgentInfosWrapper explorationMethodDescriptions =
				knownMethods.exportExplorationAgentInfos();
		List<Class<?>> explorationAgentTypes =
				explorationMethodDescriptions.exportAgentTypes();

		MethodsStatistics resultOfExplorationMethods = currentMethodsResults.
				getMethodResultsContainsInstancesOf(explorationAgentTypes);

		
		// get ratio of exploration and exploitation		
		double ratioExplorationDivSize = countRation(currentMethodsResults,
				resultOfExploitationMethods, resultOfExplorationMethods);
		
		double ratioIteration = iteration.exportRationOfIteration();

		if (ratioExplorationDivSize + 0.1 < ratioIteration) {

			logger.log(Level.INFO, "Less Exploitation, more Exploration methods");
			
			// choose best method to duplicate
			History historyExploration =
					history.exportHistoryOfAgents(explorationAgentTypes);
			MethodsStatistics explorationMethodsResults =
					historyExploration.exportMethodsResults(iteration);
			MethodStatistic methodToCreateI = explorationMethodsResults.
					exportMethodAchievedTheBestAverageOfFitness();
			AgentDescription methodToCreate =
					methodToCreateI.exportAgentDescriptionClone();
			
			//choose method to kill
			MethodStatistic methodTheLeastQuantityOfType =
					resultOfExploitationMethods.exportMethodAchievedTheLeastQuantityOfType();
			AgentDescription methodToKill = 
					methodTheLeastQuantityOfType.exportAgentDescriptionClone();
			
			return new InputRePlan(iteration, methodToKill,
					methodToCreate.exportInputAgentDescription());
		}
		
		return new InputRePlan(iteration);
	}
	
	private double countRation(MethodsStatistics currentMethodsResults,
			MethodsStatistics resultOfExploitationMethods,
			MethodsStatistics resultOfExplorationMethods) {
		
		int numOfAllAgent = currentMethodsResults.getNumberOfMethodsStatistics();
		
		int numOfExploitationAgentTypes =
				resultOfExploitationMethods.getNumberOfMethodsStatistics();
		
		int numOfExplorationAgentTypes = numOfAllAgent - numOfExploitationAgentTypes;
		
		double ratioExplorationDivSize = (double)numOfExplorationAgentTypes / (double) numOfAllAgent;
		logger.log(Level.INFO, "Exploitation / Exploration = " +
				numOfExploitationAgentTypes + " / " + numOfExplorationAgentTypes);

		return ratioExplorationDivSize;
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
		
	}

}
