package org.distributedea.problems.tsp.gps.permutation.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;


public class OperatorSimpleSwap {

	public static Individual create(IndividualPermutation individualPerm, IAgentLogger logger) {
				
		List<Integer> permutation = individualPerm.getPermutation();
		
		// copy
		List<Integer> permutationNew = new ArrayList<Integer>();
		for (int numberI : permutation) {
			permutationNew.add(numberI);
		}
		
		Random random = new Random();
		int rndFirstIndex = random.nextInt(permutationNew.size());
		int rndSecondIndex = random.nextInt(permutationNew.size());
		
		
		int firstVaLue = permutationNew.get(rndFirstIndex);
		int secondVaLue = permutationNew.get(rndSecondIndex);
		
		permutationNew.set(rndFirstIndex, secondVaLue);
		permutationNew.set(rndSecondIndex, firstVaLue);
		
		return new IndividualPermutation(permutationNew);
	}

}
