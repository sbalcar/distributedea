package org.distributedea.agents.systemagents.centralmanager.planners.historybased;

import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.monitor.MethodStatistic;

public class PlannerTheGreatestQuantityOfMaterial extends PlannerTheGreatestQuantityOfImprovement {

	protected InputRePlan replanning(Iteration iteration, History history)
			throws Exception {

		History historyOfCurrentMethods = history.
				exportHistoryOfRunningMethods(iteration, 3);
				
		if (historyOfCurrentMethods.getNumberOfMethodInstances() == 0) {
			return new InputRePlan(iteration);
		}
		
		MethodsStatistics currentMethodsResults = history.exportMethodsResults(iteration);
		MethodStatistic greatestQuantMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheGreatestQuantityOfType();
		MethodStatistic leastQuantMethodStatistic = currentMethodsResults.
				exportMethodAchievedTheLeastQuantityOfType();
		
		AgentDescription methodToKill =
				leastQuantMethodStatistic.exportAgentDescriptionClone();
		AgentDescription methodGreatestQuant =
				greatestQuantMethodStatistic.exportAgentDescriptionClone();
		
		InputAgentDescription candidateMethod = plannerInit.removeNextCandidate();
		if (candidateMethod != null) {

			return new InputRePlan(iteration, methodToKill, candidateMethod);
		}
		
		return new InputRePlan(iteration, methodToKill,
				methodGreatestQuant.exportInputAgentDescription());
	}

}
