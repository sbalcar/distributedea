package org.distributedea.agents;

import org.distributedea.ontology.problem.Problem;

public class FitnessTool {

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
}
