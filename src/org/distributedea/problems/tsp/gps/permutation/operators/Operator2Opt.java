package org.distributedea.problems.tsp.gps.permutation.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.individuals.IndividualPermutation;

public class Operator2Opt {

	public static IndividualPermutation create(IndividualPermutation individualPerm) {
		
		// copy
		List<Integer> permutationNew = new ArrayList<Integer>();
		for (int numberI : individualPerm.getPermutation()) {
			permutationNew.add(numberI);
		}
		
		Random random = new Random();
		int startIndex = random.nextInt(permutationNew.size());
		int endIndex = random.nextInt(permutationNew.size());
		
		if (endIndex < startIndex) {
			int help = startIndex;
			startIndex = endIndex;
			endIndex = help;
		}
		
		long betweenSize = Math.abs(startIndex -endIndex);
		for (int i = 0; i < betweenSize /2; i++) {
			
			int firstIndex = startIndex + i;
			int secondIndex = endIndex - i;
			
			int firstVaLue = permutationNew.get(firstIndex);
			int secondVaLue = permutationNew.get(secondIndex);
			
			permutationNew.set(firstIndex, secondVaLue);
			permutationNew.set(secondIndex, firstVaLue);
		}
		
		return new IndividualPermutation(permutationNew);
	}

}
