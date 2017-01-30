package org.distributedea.structures.comparators;

import java.util.Comparator;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

/**
 * Comparator from the best(first in list) to the worst(last in list)
 * of {@link IndividualWrapper} ontology 
 * @author stepan
 *
 */
public class CmpIndividualWrapper implements Comparator<IndividualWrapper> {

	private IProblem problem;

	/**
	 * Constructor
	 * @param problem
	 */
	public CmpIndividualWrapper(IProblem problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
		this.problem = problem;
	}
	
	@Override
	public int compare(IndividualWrapper o1, IndividualWrapper o2) {
		
		double fitness1 = o1.getIndividualEvaluated().getFitness();
		double fitness2 = o2.getIndividualEvaluated().getFitness();
		
		if (fitness1 == fitness2) {
			return 0;
		}
		
		boolean isFistFitnessBetterThanSecond = FitnessTool.
				isFistFitnessBetterThanSecond(fitness1, fitness2, problem);
		
		if (isFistFitnessBetterThanSecond) {
			return -1;
		} else {
			return 1;
		}
	}
	
}
