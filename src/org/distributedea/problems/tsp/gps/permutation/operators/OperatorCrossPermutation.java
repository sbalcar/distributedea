package org.distributedea.problems.tsp.gps.permutation.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.individuals.IndividualPermutation;

public class OperatorCrossPermutation {

	public static IndividualPermutation crossover(IndividualPermutation individual0,
			IndividualPermutation individual1) {

		List<Integer> permitation0 = individual0.getPermutation();

		Random random = new Random();
		int rndFirstIndex = random.nextInt(permitation0.size());
		int rndSecondIndex = random.nextInt(permitation0.size());

		int startIndex = Math.min(rndFirstIndex, rndSecondIndex);
		int endIndex = Math.max(rndFirstIndex, rndSecondIndex);		
		
		List<Integer> subpermitation0 = new ArrayList<>();
		for (int i = startIndex; i < endIndex; i++) {
			subpermitation0.add(permitation0.get(i));
		}
		
		List<Integer> supplementOfPermitation1 = new ArrayList<>();
		for (int valueI : individual1.getPermutation()) {
			if (! subpermitation0.contains(valueI)) {
				supplementOfPermitation1.add(valueI);
			}
		}
		
		List<Integer> newPermitation = new ArrayList<>();
		newPermitation.addAll(subpermitation0);
		newPermitation.addAll(supplementOfPermitation1);
		
		return new IndividualPermutation(newPermitation);
	}
}
