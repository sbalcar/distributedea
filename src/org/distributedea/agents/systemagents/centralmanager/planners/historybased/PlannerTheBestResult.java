package org.distributedea.agents.systemagents.centralmanager.planners.historybased;

import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.monitor.MethodStatistic;

public class PlannerTheBestResult extends PlannerTheGreatestQuantityOfImprovement {

	protected InputRePlan replanning(Iteration iteration, History history)
			throws Exception {

		History historyOfCurrentMethods = history.
				exportHistoryOfRunningMethods(iteration, 3);
		
		if (historyOfCurrentMethods.getNumberOfMethodInstances() <= 1) {
			return new InputRePlan(iteration);
		}
		
		MethodsStatistics currentMethodsResults =
				history.exportMethodsResults(iteration);
		MethodStatistic theBestOfBestMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheBestOfBestIndividuals();
		MethodStatistic theWorstOfBestMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheWorstOfBestIndividuals();
		
		AgentDescription methodToKill =
				theWorstOfBestMethodStatistic.exportAgentDescriptionClone();
		AgentDescription methodTheBestOfBest =
				theBestOfBestMethodStatistic.exportAgentDescriptionClone();
		
		InputAgentDescription candidateMethod = plannerInit.removeNextCandidate();
		if (candidateMethod != null) {

			return new InputRePlan(iteration, methodToKill, candidateMethod);
		}
		
		return new InputRePlan(iteration, methodToKill,
				methodTheBestOfBest.exportInputAgentDescription());
	}
}
