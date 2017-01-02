package org.distributedea.problems.binpacking.permutation.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemBinPacking;

public class ToolGenerateIndividualBinPacking {

	public static IndividualPermutation generateIndividual(ProblemBinPacking problemBP,
			IAgentLogger logger) {
		
		int numOfObjects = problemBP.getObjects().size();
		
		List<Integer> permutation = new ArrayList<>();
		for (int i = 0; i < numOfObjects; i++) {
			permutation.add(i);
		}
		
		Collections.shuffle(permutation);
		
		return new IndividualPermutation(permutation);
	}
}
