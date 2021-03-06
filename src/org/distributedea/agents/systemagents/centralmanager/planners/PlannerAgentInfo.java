package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfigurations;
import org.distributedea.ontology.agentinfo.AgentInfosWrapper;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problemtooldefinition.ProblemToolsDefinition;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;


public class PlannerAgentInfo implements IPlanner {

	private int DURATION_OF_NEW_METHOD_PROTECTION = 3;
	
	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IslandModelConfiguration configuration;
	private IAgentLogger logger;
	
	private IPlanner plannerInit;
	
	private AgentInfosWrapper knownMethods;
	
	
	public PlannerAgentInfo() {} // for serialization
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");

		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.configuration = configuration;
		this.logger = logger;
		
		// get all available Agent Description
		PlannerInitialisationRunEachMethodOnce planner = new PlannerInitialisationRunEachMethodOnce();
		planner.agentInitialisationOnlyCreateAgents(centralManager, iteration,
				jobRun, logger);
		
		knownMethods = ComputingAgentService.getAgentInfos(centralManager, logger);
		planner.exit(centralManager, logger);
		
		
		ProblemToolsDefinition problemTools = jobRun.getMethods().exportProblemTools();
		
		// initialize one agent per core
		AgentInfosWrapper exploitationMethodDescriptionsWrapper =
				knownMethods.exportExploitationAgentInfos();
		AgentConfigurations exploitationAgentConfigurations =
				exploitationMethodDescriptionsWrapper.exportAgentConfigurations();
		InputAgentConfigurations agentConfigurations =
				exploitationAgentConfigurations.exportInputAgentConfigurations();
		
		InputMethodDescriptions methods = new InputMethodDescriptions();
		methods.addInputMethodDescrCartesianProduct(agentConfigurations, problemTools);
		JobRun exploitationJobRun = jobRun.deepClone();		
		exploitationJobRun.setMethods(methods);
		
		plannerInit = new PlannerInitialisationOneMethodPerCore();
		return plannerInit.agentInitialisation(centralManager, iteration,
				exploitationJobRun, configuration, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, configuration, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
			 ) throws Exception {
		// counts ration of exploitation and exploration method
		ExploitationExplorationRatio ration = countRation(iteration,
				history.getJobID(), history.getMethodHistories());
		
		// print info
		printLog(centralManager, ration, iteration, history, logger);
		
		
		MethodHistories current3MethodHistories = history.getMethodHistories()
				.exportHistoryOfRunningMethods(iteration, DURATION_OF_NEW_METHOD_PROTECTION);
		
		if (current3MethodHistories.getNumberOfMethodInstances() <= 1) {
			return new InputRePlan(iteration);
		}
		
		MethodsStatistics current3MethodsStats = current3MethodHistories
				.exportMethodsResults(iteration, history.getJobID());
		
		
		ProblemToolsDefinition problemTools = jobRun.getMethods().exportProblemTools();

		
		AgentInfosWrapper exploitationMethodInfosWrp =
				knownMethods.exportExploitationAgentInfos();
		AgentConfigurations exploitationAgentConfigurations =
				exploitationMethodInfosWrp.exportAgentConfigurations();
		InputAgentConfigurations exploitationInAgentConfs =
				exploitationAgentConfigurations.exportInputAgentConfigurations();

	
		InputMethodDescriptions methods = new InputMethodDescriptions();
		methods.addInputMethodDescrCartesianProduct(exploitationInAgentConfs, problemTools);
		JobRun exploitationJobRun = jobRun.deepClone();
		exploitationJobRun.setMethods(methods);
				
		
		InputMethodDescriptions exploitationMethodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(exploitationJobRun);
		
		if (! exploitationMethodsWhichHaveNeverRun.isEmpty()) {
			// remove worst and replace by new candidate
			logger.log(Level.INFO, "Trying next candidate");
			
			MethodStatistic methodStatistic = current3MethodsStats.
					exportMethodAchievedTheLeastQuantityOfImprovement();
			MethodDescription methodToKill = methodStatistic.
					exportMethodDescriptionClone();

			InputMethodDescription candidateMethod =
					exploitationMethodsWhichHaveNeverRun.exportRandomMethodDescription();
			return new InputRePlan(iteration, methodToKill, candidateMethod);
		}
		
		
		// get ratio of exploration and exploitation
		if (ration.getRatio() < iteration.exportRation()) {

			logger.log(Level.INFO, "Less Exploitation, more Exploration methods");
			
			// choose best method to duplicate
			AgentInfosWrapper explorationMethodDescriptions =
					knownMethods.exportExplorationAgentInfos();
			List<Class<?>> explorationAgentTypes =
					explorationMethodDescriptions.exportAgentTypes();
			MethodsStatistics explorationMethodsResults =
					history.exportMethodsStatisticsOfAgent(explorationAgentTypes, iteration);

			MethodStatistic methodStatisticToCreate = explorationMethodsResults.
					exportMethodAchievedTheBestAverageOfFitness();
			InputMethodDescription methodToCreate =
					methodStatisticToCreate.exportInputMethodDescriptionClone();
			
			// choose method to kill
			AgentInfosWrapper exploitationMethodDescriptions =
					knownMethods.exportExploitationAgentInfos();
			List<Class<?>> exploitationAgentTypes = 
					exploitationMethodDescriptions.exportAgentTypes();
			MethodsStatistics exploitationMethodStatistics = current3MethodsStats.
					getMethodStatisticsOfAgentClasses(exploitationAgentTypes);
			
			MethodStatistic methodTheLeastQuantityOfType =
					exploitationMethodStatistics.exportMethodAchievedTheLeastQuantityOfType();
			MethodDescription methodToKill = 
					methodTheLeastQuantityOfType.exportMethodDescriptionClone();
			
			return new InputRePlan(iteration, methodToKill, methodToCreate);
		}
		
		return new InputRePlan(iteration);
	}
	
	private void printLog(Agent_CentralManager centralManager,
			ExploitationExplorationRatio ratio, Iteration iteration,
			History history, IAgentLogger logger) {

		logger.log(Level.INFO, "Exploitation / Exploration = " +
				ratio.numOfExploitationAgentTypes + " / " + ratio.numOfExplorationAgentTypes);
	}

	private ExploitationExplorationRatio countRation(Iteration iteration,
			JobID jobID, MethodHistories currentMethodHistories) {
		
		MethodsStatistics currentMethodsResults = currentMethodHistories
				.exportMethodsResults(iteration, jobID);
		
		AgentInfosWrapper exploitationMethodDescriptions =
				knownMethods.exportExploitationAgentInfos();
		List<Class<?>> exploitationAgentTypes = 
				exploitationMethodDescriptions.exportAgentTypes();
		
		MethodsStatistics exploitationMethodStatistics = currentMethodsResults.
				getMethodStatisticsOfAgentClasses(exploitationAgentTypes);
		
		
		AgentInfosWrapper explorationMethodDescriptions =
				knownMethods.exportExplorationAgentInfos();
		List<Class<?>> explorationAgentTypes =
				explorationMethodDescriptions.exportAgentTypes();

		MethodsStatistics explorationMethodStatistics = currentMethodsResults.
				getMethodStatisticsOfAgentClasses(explorationAgentTypes);
		
		
		int numOfAllAgent =
				currentMethodsResults.getNumberOfMethodsStatistics();
		int numOfExploitationAgentTypes =
				exploitationMethodStatistics.getNumberOfMethodsStatistics();
		int numOfExplorationAgentTypes = 
				explorationMethodStatistics.getNumberOfMethodsStatistics();

		
		ExploitationExplorationRatio ratio = new ExploitationExplorationRatio();
		ratio.numOfExploitationAgentTypes = numOfExploitationAgentTypes;
		ratio.numOfExplorationAgentTypes = numOfExplorationAgentTypes;
		ratio.numOfAllAgent = numOfAllAgent;
		
		return ratio;
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
		
	}

}


class ExploitationExplorationRatio {
	
	int numOfExploitationAgentTypes;
	int numOfExplorationAgentTypes;
	int numOfAllAgent;
	
	public double getRatio() {
		int numOfExplorationAgentTypes_ = numOfAllAgent - numOfExploitationAgentTypes;
		return (double)numOfExplorationAgentTypes_ / (double) numOfAllAgent;
	}
}
