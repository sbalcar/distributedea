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


public class PlannerTheBestResult extends PlannerTheGreatestQuantityOfImprovement {

	private int DURATION_OF_NEW_METHOD_PROTECTION = 3;
	
	@Override
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
				history.exportMethodsResults(iteration);
		MethodStatistic theBestOfBestMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheBestOfBestIndividuals();
		MethodStatistic theWorstOfBestMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheWorstOfBestIndividuals();
		
		MethodDescription methodToKill =
				theWorstOfBestMethodStatistic.exportMethodDescriptionClone();
		InputMethodDescription methodTheBestOfBest =
				theBestOfBestMethodStatistic.exportInputMethodDescriptionClone();
		
		
		InputMethodDescriptions methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
		if (! methodsWhichHaveNeverRun.isEmpty()) {

			InputMethodDescription candidateMethod =
					methodsWhichHaveNeverRun.exportRandomMethodDescription();
			
			return new InputRePlan(iteration, methodToKill, candidateMethod);
		}
		
		return new InputRePlan(iteration, methodToKill, methodTheBestOfBest).
				exportOptimalizedInpuRePlan();
	}
	
	private void printLog(Agent_CentralManager centralManager,
			Iteration iteration, History history, IAgentLogger logger) {
		
		MethodsStatistics currentMethodsResults = history.getMethodHistories()
				.exportMethodsResults(iteration, history.getJobID());
		MethodStatistic theBestMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheBestOfBestIndividuals();
		MethodStatistic theWorstMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheWorstOfBestIndividuals();
		
		String theWorstAgentName = theWorstMethodStatistic.
				getMethodDescription().getAgentConfiguration().exportAgentname();
		double theWorstFitness = theWorstMethodStatistic.
				exportBestIndividual().getFitness();
		
		logger.log(Level.INFO, "The Worst BestIndividual: " + theWorstAgentName + " fitness: " + theWorstFitness);

		
		String theBestAgentName = theBestMethodStatistic.
				getMethodDescription().getAgentConfiguration().exportAgentname();
		double theBestFitness = theBestMethodStatistic.
				exportBestIndividual().getFitness();
		
		logger.log(Level.INFO, "The Best BestIndividual : " + theBestAgentName + " fitness: " + theBestFitness);
	}
	
}
