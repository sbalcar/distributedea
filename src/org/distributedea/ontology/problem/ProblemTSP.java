package org.distributedea.ontology.problem;

import org.distributedea.ontology.problem.tsp.Position;

public abstract class ProblemTSP extends Problem {

	private static final long serialVersionUID = 1L;

	public abstract Position exportPosition(int itemNumber);
	public abstract int numberOfPositions();
	
	@Override
	public boolean isMaximizationProblem() {
		return false;
	}
	
}
