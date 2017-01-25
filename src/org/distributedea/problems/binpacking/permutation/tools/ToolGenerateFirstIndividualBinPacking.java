package org.distributedea.problems.binpacking.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolGenerateFirstIndividualBinPacking {

	public static IndividualPermutation generateFirstIndividual(DatasetBinPacking problemBP,
			IAgentLogger logger) {
		
		return ToolGenerateIndividualBinPacking.generateIndividual(problemBP, logger);
	}
}
