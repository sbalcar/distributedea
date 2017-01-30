package org.distributedea.problems.tsp.gps;

import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problems.tsp.ProblemTSPTool;

public abstract class ProblemTSPGPSTool extends ProblemTSPTool {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetTSPGPS.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPGPS.class;
	}
}
