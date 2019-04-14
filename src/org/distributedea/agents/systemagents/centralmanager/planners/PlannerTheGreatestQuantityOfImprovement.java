package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;


public class PlannerTheGreatestQuantityOfImprovement implements IPlanner {

	private int DURATION_OF_NEW_METHOD_PROTECTION = 3;
	
	protected Agent_CentralManager centralManager;
	protected JobRun jobRun;
	private IslandModelConfiguration configuration;
	protected IAgentLogger logger;
	
	protected IPlanner plannerInit = null;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {

		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.configuration = configuration;
		this.logger = logger;
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		
		plannerInit = new PlannerInitialisationOneMethodPerCore();
		return plannerInit.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history)
			throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		
		
		// correct empty re-planning
		if (rePlan.isEmpty() &&
				history.wereLastKRePlanEmpty(iteration, 10)) {
			logger.log(Level.INFO, "correct replanning");
			rePlan = correctReplanning(iteration, history);
		}
		
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, configuration, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	protected InputRePlan replanning(Iteration iteration, History history)
			throws Exception {

		// pring info
		printLog(centralManager, iteration, history, logger);
		
		MethodHistories currentMethodsHistory = history.getMethodHistories()
				.exportHistoryOfRunningMethods(iteration, DURATION_OF_NEW_METHOD_PROTECTION);
		
		if (currentMethodsHistory.getNumberOfMethodInstances() <= 1) {
			return new InputRePlan(iteration);
		}
		
		MethodsStatistics currentMethodsResults =
				currentMethodsHistory.exportMethodsResults(iteration, history.getJobID());
		MethodStatistic greatestQuantMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheGreatestQuantityOfImprovement();
		MethodStatistic leastQuantMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheLeastQuantityOfImprovement();
		
		MethodDescription methodToKill =
				leastQuantMethodStatistic.exportMethodDescriptionClone();
		InputMethodDescription methodGreatestQuant =
				greatestQuantMethodStatistic.exportInputMethodDescriptionClone();
		
		
		InputMethodDescriptions methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
		
		if (! methodsWhichHaveNeverRun.isEmpty()) {

			InputMethodDescription candidateMethod =
					methodsWhichHaveNeverRun.exportRandomMethodDescription();
			
			return new InputRePlan(iteration, methodToKill,	candidateMethod);
		}
		
		return new InputRePlan(iteration, methodToKill, methodGreatestQuant).
				exportOptimalizedInpuRePlan();
	}

	protected InputRePlan correctReplanning(Iteration iteration, History history)
			throws Exception {
		
		MethodHistories currentMethodsHistory = history.getMethodHistories()
				.exportHistoryOfRunningMethods(iteration, 0);

		//random select agent to kill
		MethodDescription methodToKill =
				currentMethodsHistory.exportRandomRunningMethod();
		
		MethodType methodTypeNotRunForTheLongestTime =
				history.methodsWhichDidntRunForTheLongestTime(
						jobRun.getMethods().exportInputMethodDescriptions().exportMethodTypes());

		InputMethodDescription methodToCreate =
				methodTypeNotRunForTheLongestTime.exportInputMethodDescription();
		
		return new InputRePlan(iteration, methodToKill, methodToCreate);
	}
	
	private void printLog(Agent_CentralManager centralManager,
			Iteration iteration, History history, IAgentLogger logger) {
		
		MethodsStatistics currentMethodsResults = history.getMethodHistories()
				.exportMethodsResults(iteration, history.getJobID());
		MethodStatistic greatestQuantityMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheGreatestQuantityOfImprovement();
		MethodStatistic leastQuantityMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheLeastQuantityOfImprovement();
		
		String minPriorityAgentName = leastQuantityMethodStatistic.
				getMethodDescription().getAgentConfiguration().exportAgentname();
		int leastQuantity = leastQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfTheBestCreatedIndividuals();
		
		logger.log(Level.INFO, "The least Quantity: " + minPriorityAgentName + " quantity of improvement: " + leastQuantity);

		
		String maxPriorityAgentName = greatestQuantityMethodStatistic.
				getMethodDescription().getAgentConfiguration().exportAgentname();
		int greatestQuantity = greatestQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfTheBestCreatedIndividuals();
		
		logger.log(Level.INFO, "The greatest Quantity : " + maxPriorityAgentName + " quantity of improvement: " + greatestQuantity);
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
