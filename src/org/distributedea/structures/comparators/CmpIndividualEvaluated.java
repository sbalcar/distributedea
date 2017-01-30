package org.distributedea.structures.comparators;

import java.util.Comparator;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

/**
 * Comparator sorts {@link IndividualEvaluated} from best fitness to worst.
 * @author stepan
 *
 */
public class CmpIndividualEvaluated implements Comparator<IndividualEvaluated> {

	private IProblem problem;
	
	/**
	 * Constructor
	 * @param problem
	 */
	public CmpIndividualEvaluated(IProblem problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
		this.problem = problem;
	}
	
	@Override
	public int compare(IndividualEvaluated o1, IndividualEvaluated o2) {
		
		double fitness1 = o1.getFitness();
		double fitness2 = o2.getFitness();
		
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
