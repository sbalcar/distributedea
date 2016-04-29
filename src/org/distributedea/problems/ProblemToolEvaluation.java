package org.distributedea.problems;

import org.distributedea.ontology.problem.Problem;

public class ProblemToolEvaluation {

	public static boolean isFistFitnessBetterThanSecond(double fintess1,
			double fitness2, Problem problem) {
		
		if (problem.isMaximizationProblem()) {
			
			if (fintess1 > fitness2) {
				return true;
			}
		
		} else {

			if (fintess1 < fitness2) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean isFistFitnessWorseThanSecond(double fintess1,
			double fitness2,  Problem problem) {
		
		boolean isBetter = isFistFitnessBetterThanSecond(fintess1,
				fitness2, problem);
		boolean isEqual = isFistFitnessEqualToSecond(fintess1,
				fitness2, problem);
		
		if ( (! isBetter) && (! isEqual) ) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isFistFitnessEqualToSecond(double fintess1,
			double fitness2, Problem problem) {
		return (fintess1 == fitness2);
	}
}
