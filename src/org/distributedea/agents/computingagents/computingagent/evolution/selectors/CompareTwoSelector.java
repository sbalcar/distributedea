package org.distributedea.agents.computingagents.computingagent.evolution.selectors;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problem.Problem;

public class CompareTwoSelector implements ISelector {

	public IndividualEvaluated select(IndividualsEvaluated individuals, Problem problem) {
		
		IndividualEvaluated individual1 =
				individuals.exportRandomIndividualEvaluated();
		IndividualEvaluated individual2 =
				individuals.exportRandomIndividualEvaluated();
		
		boolean isFirstIndividualBetter =
				FitnessTool.isFirstIndividualEBetterThanSecond(
						individual1, individual2, problem);
		
		if (isFirstIndividualBetter) {
			
			return individual1;
		} else {
			
			return individual2;
		}
	}
}
