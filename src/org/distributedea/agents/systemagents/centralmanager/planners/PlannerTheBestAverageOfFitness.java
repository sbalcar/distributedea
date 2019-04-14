package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.monitor.MethodStatistic;


public class PlannerTheBestAverageOfFitness extends PlannerTheGreatestQuantityOfImprovement {

	private int DURATION_OF_NEW_METHOD_PROTECTION = 3;
	
	@Override
	protected InputRePlan replanning(Iteration iteration, History history)
			throws Exception {

		// pring info
		printLog(centralManager, iteration, history, logger);
		
		MethodHistories currentMethodsHistory = history.getMethodHistories()
				.exportHistoryOfRunningMethods(iteration, DURATION_OF_NEW_METHOD_PROTECTION);
		
		if (currentMethodsHistory.getNumberOfMethodInstances() == 0) {
			return new InputRePlan(iteration);
		}
		
		MethodsStatistics currentMethodsResults = history.exportMethodsResults(iteration);
		MethodStatistic bestAverageMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheBestAverageOfFitness();
		MethodStatistic worstAverageMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheWorstAverageOfFitness();
		
		MethodDescription methodToKill =
				worstAverageMethodStatistic.exportMethodDescriptionClone();
		InputMethodDescription methodGreatestQuant =
				bestAverageMethodStatistic.exportInputMethodDescriptionClone();
		

		InputMethodDescriptions methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
		
		if (! methodsWhichHaveNeverRun.isEmpty()) {

			InputMethodDescription candidateMethod =
					methodsWhichHaveNeverRun.exportRandomMethodDescription();
			
			return new InputRePlan(iteration, methodToKill,
					candidateMethod);
		}

		return new InputRePlan(iteration, methodToKill, methodGreatestQuant).
				exportOptimalizedInpuRePlan();
	}

	private void printLog(Agent_CentralManager centralManager,
			Iteration iteration, History history, IAgentLogger logger) {
		
		MethodsStatistics currentMethodsResults = history.getMethodHistories()
				.exportMethodsResults(iteration, history.getJobID());
		MethodStatistic theBestAverageMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheBestAverageOfFitness();
		MethodStatistic theWorstAverageMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheWorstAverageOfFitness();
		
		String minPriorityAgentName = theWorstAverageMethodStatistic.
				getMethodDescription().getAgentConfiguration().exportAgentname();
		int theWorstAverage = theWorstAverageMethodStatistic.
				getMethodStatisticResult().getNumberOfTypeIndividuals();
		
		logger.log(Level.INFO, "The Worst Average: " + minPriorityAgentName + " average: " + theWorstAverage);

		
		String maxPriorityAgentName = theBestAverageMethodStatistic.
				getMethodDescription().getAgentConfiguration().exportAgentname();
		int theBestAverage = theBestAverageMethodStatistic.
				getMethodStatisticResult().getNumberOfTypeIndividuals();
		
		logger.log(Level.INFO, "The Best Average : " + maxPriorityAgentName + " average: " + theBestAverage);
	}
	
}
