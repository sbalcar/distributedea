package org.distributedea.agents.systemagents.centralmanager.planners.historybased;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.monitor.MethodStatistic;

public class PlannerTheGreatestQuantityOfGoodMaterial extends PlannerTheGreatestQuantityOfImprovement {

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
				exportMethodAchievedTheGreatestQuantityOfGoodMaterial();
		MethodStatistic leastQuantMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheLeastQuantityOfGoodMaterial();
		
		AgentDescription methodToKill =
				leastQuantMethodStatistic.exportAgentDescriptionClone();
		InputAgentDescription methodGreatestQuant =
				greatestQuantMethodStatistic.exportInputAgentDescriptionClone();
		
		
		InputAgentDescriptions methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
				
		if (! methodsWhichHaveNeverRun.isEmpty()) {
			
			InputAgentDescription candidateMethod =
					methodsWhichHaveNeverRun.exportRandomInputAgentDescription();

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
				exportMethodAchievedTheGreatestQuantityOfGoodMaterial();
		MethodStatistic leastQuantityMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheLeastQuantityOfGoodMaterial();
		
		String minPriorityAgentName = leastQuantityMethodStatistic.
				getAgentDescription().getAgentConfiguration().exportAgentname();
		int leastGoodMaterialQuantity = leastQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfGoodCreatedMaterial();
		
		logger.log(Level.INFO, "The least Quantity : " + minPriorityAgentName + " good material quantity: " + leastGoodMaterialQuantity);

		
		String maxPriorityAgentName = greatestQuantityMethodStatistic.
				getAgentDescription().getAgentConfiguration().exportAgentname();
		int greatestGoodMaterailQuantity = greatestQuantityMethodStatistic.
				getMethodStatisticResult().getNumberOfGoodCreatedMaterial();
		
		logger.log(Level.INFO, "The greatest Quantity : " + maxPriorityAgentName + " good material quantity: " + greatestGoodMaterailQuantity);
	}
}
