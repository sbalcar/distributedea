package org.distributedea.problemtools.binpacking;

import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problemtools.tsp.AProblemToolTSP;

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
