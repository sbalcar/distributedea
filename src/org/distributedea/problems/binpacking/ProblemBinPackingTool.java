package org.distributedea.problems.binpacking;

import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.problemdefinition.ProblemTSPGPSDef;
import org.distributedea.problems.tsp.ProblemTSPTool;

public abstract class ProblemBinPackingTool extends ProblemTSPTool {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetBinPacking.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPGPSDef.class;		
	}
}
