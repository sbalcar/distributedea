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
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;

public class PlannerLazyQuantityOfImprovement implements IPlanner {

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
				
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, configuration, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	protected InputRePlan replanning(Iteration iteration, History history)
			throws Exception {

		// pring info
		printLog(centralManager, iteration, history, logger);

		MethodHistories currentMethodsHistory = history.getMethodHistories()
				.exportHistoryOfRunningMethods(iteration, 3);
		
		if (currentMethodsHistory.getNumberOfMethodInstances() <= 1) {
			return new InputRePlan(iteration);
		}


		Methods methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
		
		if (! methodsWhichHaveNeverRun.isEmpty()) {

			MethodsStatistics currentMethodsResults =
					currentMethodsHistory.exportMethodsResults(iteration, history.getJobID());

			MethodStatistic leastQuantMethodStatistic = currentMethodsResults
					.exportMethodAchievedTheLeastQuantityOfImprovement();
			
			MethodDescription methodToKill =
					leastQuantMethodStatistic.exportAgentDescriptionClone();

			InputMethodDescription candidateMethod =
					methodsWhichHaveNeverRun.exportRandomSelectedAgentDescription();
			
			return new InputRePlan(iteration, methodToKill,	candidateMethod);
		}
				
				
		MethodsStatistics currentMethodsResults =
				currentMethodsHistory.exportMethodsResults(iteration, history.getJobID());
		
		MethodStatistic greatestQuantMethodStatistic = currentMethodsResults
				.exportMethodAchievedTheGreatestQuantityOfImprovement();
		
		
		MethodHistories currentleastQuantMethodsHistory = currentMethodsHistory
				.exportHistoryWithoutImprovement(iteration, 3);
		
		MethodsStatistics leastQuantMethodStatistics =
				currentleastQuantMethodsHistory.exportMethodsResults(iteration, history.getJobID());
		
		MethodDescriptions leastQuantMethods =
				leastQuantMethodStatistics.exportAgentDescriptions();
		
		if (leastQuantMethods.isEmpty()) {
			return new InputRePlan(iteration);
		}
		
		
		MethodDescription methodToKill =
				leastQuantMethods.exportRandomAgentDescription();
		InputMethodDescription methodGreatestQuant =
				greatestQuantMethodStatistic.exportInputAgentDescriptionClone();
				
		return new InputRePlan(iteration, methodToKill, methodGreatestQuant).
				exportOptimalizedInpuRePlan();
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
				getAgentDescription().getAgentConfiguration().exportAgentname();
		int leastQuantity = leastQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfTheBestCreatedIndividuals();
		
		logger.log(Level.INFO, "The least Quantity: " + minPriorityAgentName + " quantity of improvement: " + leastQuantity);

		
		String maxPriorityAgentName = greatestQuantityMethodStatistic.
				getAgentDescription().getAgentConfiguration().exportAgentname();
		int greatestQuantity = greatestQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfTheBestCreatedIndividuals();
		
		logger.log(Level.INFO, "The greatest Quantity : " + maxPriorityAgentName + " quantity of improvement: " + greatestQuantity);
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
