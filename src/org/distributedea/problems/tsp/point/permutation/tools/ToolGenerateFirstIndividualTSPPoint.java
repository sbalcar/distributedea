package org.distributedea.problems.tsp.point.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolGenerateFirstIndividualTSPPoint {

	public static IndividualPermutation generate(DatasetTSPPoint dataset,
			IAgentLogger logger) {
		
		return ToolGenerateIndividualForTSPPoint.generate(dataset);
	}
}
