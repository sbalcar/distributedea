package org.distributedea.agents.computingagents.computingagent.evolution.selectors;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;

public class CompareTwoSelector implements ISelector {

	public IndividualEvaluated select(IndividualsEvaluated individuals, IProblemDefinition problemDef) {
		
		IndividualEvaluated individual1 =
				individuals.exportRandomIndividualEvaluated();
		IndividualEvaluated individual2 =
				individuals.exportRandomIndividualEvaluated();
		
		boolean isFirstIndividualBetter =
				FitnessTool.isFirstIndividualEBetterThanSecond(
						individual1, individual2, problemDef);
		
		if (isFirstIndividualBetter) {
			
			return individual1;
		} else {
			
			return individual2;
		}
	}
}
