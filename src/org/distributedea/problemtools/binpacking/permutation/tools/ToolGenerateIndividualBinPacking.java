package org.distributedea.problemtools.binpacking.permutation.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolGenerateIndividualBinPacking {

	public static IndividualPermutation generateIndividual(DatasetBinPacking datasetBP,
			IAgentLogger logger) {
		
		int numOfObjects = datasetBP.getObjects().size();
		
		List<Integer> permutation = new ArrayList<>();
		for (int i = 0; i < numOfObjects; i++) {
			permutation.add(i);
		}
		
		Collections.shuffle(permutation);
		
		return new IndividualPermutation(permutation);
	}
}
