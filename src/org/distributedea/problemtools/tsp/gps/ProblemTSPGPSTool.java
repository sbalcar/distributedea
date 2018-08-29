package org.distributedea.problemtools.tsp.gps;

import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problemtools.tsp.AProblemToolTSP;

public abstract class ProblemTSPGPSTool extends AProblemToolTSP {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetTSPGPS.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPGPS.class;
	}
}
