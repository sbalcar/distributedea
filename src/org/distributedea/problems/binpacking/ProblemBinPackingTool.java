package org.distributedea.problems.binpacking;

import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.problems.tsp.ProblemTSPTool;

public abstract class ProblemBinPackingTool extends ProblemTSPTool {

	@Override
	public Class<?> problemWhichSolves() {
		return ProblemBinPacking.class;
	}
}
