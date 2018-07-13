package org.distributedea.problems.binpacking;

import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problems.tsp.AProblemToolTSP;

public abstract class ProblemBinPackingTool extends AProblemToolTSP {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetBinPacking.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPGPS.class;		
	}
}
