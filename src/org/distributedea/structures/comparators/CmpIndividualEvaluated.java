package org.distributedea.structures.comparators;

import java.util.Comparator;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;

/**
 * Comparator sorts {@link IndividualEvaluated} from best fitness to worst.
 * @author stepan
 *
 */
public class CmpIndividualEvaluated implements Comparator<IndividualEvaluated> {

	private IProblemDefinition problemDef;
	
	/**
	 * Constructor
	 * @param problem
	 */
	public CmpIndividualEvaluated(IProblemDefinition problemDef) {
		if (problemDef == null || ! problemDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Problem.class.getSimpleName() + " is not valid");
		}
		this.problemDef = problemDef;
	}
	
	@Override
	public int compare(IndividualEvaluated o1, IndividualEvaluated o2) {
		
		double fitness1 = o1.getFitness();
		double fitness2 = o2.getFitness();
		
		if (fitness1 == fitness2) {
			return 0;
		}
		
		boolean isFistFitnessBetterThanSecond = FitnessTool.
				isFistFitnessBetterThanSecond(fitness1, fitness2, problemDef);
		
		if (isFistFitnessBetterThanSecond) {
			return -1;
		} else {
			return 1;
		}
	}

}
