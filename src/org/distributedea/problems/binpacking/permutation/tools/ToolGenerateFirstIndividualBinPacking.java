package org.distributedea.problems.binpacking.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemBinPacking;

public class ToolGenerateFirstIndividualBinPacking {

	public static IndividualPermutation generateFirstIndividual(ProblemBinPacking problemBP,
			IAgentLogger logger) {
		
		return ToolGenerateIndividualBinPacking.generateIndividual(problemBP, logger);
	}
}
