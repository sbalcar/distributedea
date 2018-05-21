package org.distributedea.problems.tsp.point;

import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.ProblemToolTSP;

public abstract class ProblemTSPPointTool extends ProblemToolTSP {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetTSPPoint.class;
	}

	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPPoint.class;
	}
}
