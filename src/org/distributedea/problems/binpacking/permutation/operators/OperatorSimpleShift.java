package org.distributedea.problems.binpacking.permutation.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class OperatorSimpleShift {

	public static Individual create(IndividualPermutation individualPerm, IAgentLogger logger) {
	
		List<Integer> permutation = individualPerm.getPermutation();
		
		// copy
		List<Integer> permutationNew = new ArrayList<Integer>();
		for (int numberI : permutation) {
			permutationNew.add(numberI);
		}
		
		Random random = new Random();
		int rndIndex = random.nextInt(permutationNew.size());
		
		int vaLue = permutationNew.get(rndIndex);
		
		permutationNew.remove(rndIndex);
		permutationNew.add(vaLue);
		
		return new IndividualPermutation(permutationNew);
	}
}
