package org.distributedea.problemtools.binpacking.permutation.tools;

import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolNextPermutationTSPGPS;

public class ToolNextPermutationBinPacking {

	public static IndividualPermutation nextPermutation(IndividualPermutation individual) {
		 
		return ToolNextPermutationTSPGPS.nextPermutation(individual);
	}
}
