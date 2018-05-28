package org.distributedea.agents.computingagents.specific.evolution.selectors;

import java.util.Random;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

public class CompareTwoSelector implements ISelector {

	public IndividualEvaluated select(IndividualEvaluated[] individuals, IProblem problem) {
		
		Random ran = new Random();

		IndividualEvaluated individual1 =
				individuals[ran.nextInt(individuals.length)];
		IndividualEvaluated individual2 =
				individuals[ran.nextInt(individuals.length)];
		
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
