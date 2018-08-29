package org.distributedea.problemtools.binpacking.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolGenerateFirstIndividualBinPacking {

	public static IndividualPermutation generateFirstIndividual(DatasetBinPacking datasetBP,
			IAgentLogger logger) {
		
		return ToolGenerateIndividualBinPacking.generateIndividual(datasetBP, logger);
	}
}
