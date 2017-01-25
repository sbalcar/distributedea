package org.distributedea.problems.tsp.point;

import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.problemdefinition.ProblemTSPPointDef;
import org.distributedea.problems.tsp.ProblemTSPTool;

public abstract class ProblemTSPPointTool extends ProblemTSPTool {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetTSPPoint.class;
	}

	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPPointDef.class;
	}
}
