package org.distributedea.problems.tsp.gps;

import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problems.tsp.ProblemTSPTool;

public abstract class ProblemTSPGPSTool extends ProblemTSPTool {

	@Override
	public Class<?> problemWhichSolves() {
		return ProblemTSPGPS.class;
	}
}
