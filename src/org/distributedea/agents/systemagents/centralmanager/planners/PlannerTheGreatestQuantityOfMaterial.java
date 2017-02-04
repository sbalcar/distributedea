package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.monitor.MethodStatistic;


public class PlannerTheGreatestQuantityOfMaterial extends PlannerTheGreatestQuantityOfImprovement {

	@Override
	protected InputRePlan replanning(Iteration iteration, History history)
			throws Exception {

		// pring info
		printLog(centralManager, iteration, history, logger);
		
		MethodHistories currentMethodsHistory = history.getMethodHistories()
				.exportHistoryOfRunningMethods(iteration, 3);
				
		if (currentMethodsHistory.isEmpty()) {
			return new InputRePlan(iteration);
		}
		
		MethodsStatistics currentMethodsResults = history.exportMethodsResults(iteration);
		MethodStatistic greatestQuantMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheGreatestQuantityOfType();
		MethodStatistic leastQuantMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheLeastQuantityOfType();
		
		MethodDescription methodToKill =
				leastQuantMethodStatistic.exportAgentDescriptionClone();
		InputMethodDescription methodGreatestQuant =
				greatestQuantMethodStatistic.exportInputAgentDescriptionClone();
		
		
		Methods methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
				
		if (! methodsWhichHaveNeverRun.isEmpty()) {
			
			InputMethodDescription candidateMethod =
					methodsWhichHaveNeverRun.exportRandomSelectedAgentDescription();

			return new InputRePlan(iteration, methodToKill, candidateMethod);
		}
		
		return new InputRePlan(iteration, methodToKill, methodGreatestQuant).
				exportOptimalizedInpuRePlan();
	}

	private void printLog(Agent_CentralManager centralManager,
			Iteration iteration, History history, IAgentLogger logger) {
		
		MethodsStatistics currentMethodsResults = history.getMethodHistories()
				.exportMethodsResults(iteration, history.getJobID());
		MethodStatistic greatestQuantityMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheGreatestQuantityOfType();
		MethodStatistic leastQuantityMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheLeastQuantityOfType();
		
		String minPriorityAgentName = leastQuantityMethodStatistic.
				getAgentDescription().getAgentConfiguration().exportAgentname();
		int leastQuantity = leastQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfTypeIndividuals();
		
		logger.log(Level.INFO, "The least Quantity: " + minPriorityAgentName + " quantity of material: " + leastQuantity);

		
		String maxPriorityAgentName = greatestQuantityMethodStatistic.
				getAgentDescription().getAgentConfiguration().exportAgentname();
		int greatestQuantity = greatestQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfTypeIndividuals();
		
		logger.log(Level.INFO, "The greatest Quantity : " + maxPriorityAgentName + " quantity of material: " + greatestQuantity);
	}
	
}