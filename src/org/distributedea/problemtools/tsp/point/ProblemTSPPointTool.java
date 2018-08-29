package org.distributedea.problemtools.tsp.point;

import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problemtools.tsp.AProblemToolTSP;

public abstract class ProblemTSPPointTool extends AProblemToolTSP {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetTSPPoint.class;
	}

	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPPoint.class;
	}
}
