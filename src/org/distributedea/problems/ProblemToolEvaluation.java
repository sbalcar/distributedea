package org.distributedea.problems;

import org.distributedea.ontology.problem.Problem;

public class ProblemToolEvaluation {

	public static boolean isFistFitnessBetterThanSecond(double fintess1,
			double fitness2,  Problem problem) {
		
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
	
}
