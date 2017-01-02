package org.distributedea.problems.tsp.point;

import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.ProblemTSPTool;

public abstract class ProblemTSPPointTool extends ProblemTSPTool {

	@Override
	public Class<?> problemWhichSolves() {
		return ProblemTSPPoint.class;
	}
}
