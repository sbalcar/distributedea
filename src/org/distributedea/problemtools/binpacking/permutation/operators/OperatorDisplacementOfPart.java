package org.distributedea.problemtools.binpacking.permutation.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class OperatorDisplacementOfPart {

	public static Individual create(IndividualPermutation individualPerm, IAgentLogger logger) {
	
		int maxPartSize = 200;
		
		List<Integer> permutation = individualPerm.getPermutation();
		
		// copy
		List<Integer> permutationCopy = new ArrayList<Integer>();
		for (int numberI : permutation) {
			permutationCopy.add(numberI);
		}
		
		Random random = new Random();
		// start of part to displace
		int rndStartIndex = random.nextInt(permutationCopy.size());
		
		// length of part to displace
		int rndPartSize = random.nextInt(permutationCopy.size()/maxPartSize);
		if (rndPartSize < 1) {
			rndPartSize = 1;
		}

		// end of part to displace
		int rndEndIndex = rndStartIndex + rndPartSize;
		if (rndEndIndex > permutationCopy.size()) {
			rndEndIndex = permutationCopy.size();
		}
		
		List<Integer> part = permutationCopy.subList(rndStartIndex, rndEndIndex);
		
		List<Integer> permutationWithoutPart = new ArrayList<>();
		for (int numI : permutationCopy) {
			if (! part.contains(numI)) {
				permutationWithoutPart.add(numI);
			}
		}
		
		int rndNewStartIndex = random.nextInt(permutationWithoutPart.size());
		List<Integer> prefix =
				permutationWithoutPart.subList(0, rndNewStartIndex);
		List<Integer> sufix =
				permutationWithoutPart.subList(rndNewStartIndex, permutationWithoutPart.size());

		
		List<Integer> permutationNew = new ArrayList<Integer>();
		permutationNew.addAll(prefix);
		permutationNew.addAll(part);
		permutationNew.addAll(sufix);
		
		return new IndividualPermutation(permutationNew);
	}
}
