package org.distributedea.agents.computingagents.computingagent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemToolEvaluation;

/**
 * Set of received Individuals from distribution
 * @author Stepan
 *
 */
public class IndividualModel {

	private List<IndividualWrapper> receivedIndividuals =
			Collections.synchronizedList(new ArrayList<IndividualWrapper>());
	
	public synchronized void add(IndividualWrapper individualW) {
		receivedIndividuals.add(individualW);
	}
	
	public synchronized IndividualWrapper getIndividual() {
		
		if (! receivedIndividuals.isEmpty()) {
			return receivedIndividuals.remove(0);
		}
		
		return new IndividualWrapper();
	}
/*
	public synchronized IndividualWrapper getBestIndividual(Problem problem) {
		
		if (receivedIndividuals.isEmpty()) {
			return null;
		}
		
		IndividualWrapper bestIndividual = receivedIndividuals.get(0);
		for (IndividualWrapper indWrapI : receivedIndividuals) {
			
			boolean isIndividualIBetter =
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							indWrapI, bestIndividual, problem);
			if (isIndividualIBetter) {
				bestIndividual = indWrapI;
			}
		}
		
		return bestIndividual;
	}
	*/
}
