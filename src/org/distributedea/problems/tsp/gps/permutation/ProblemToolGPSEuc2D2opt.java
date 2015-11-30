package org.distributedea.problems.tsp.gps.permutation;

import java.util.List;

/**
 * Problem tool for TSP Problem Represent by Permutation implements 2Opt permutation operator
 * @author stepan
 *
 */
public class ProblemToolGPSEuc2D2opt extends ProblemToolGPSEuc2DSimpleSwap {

	@Override
	protected void convertingChunkOfPermutation(int startIndex, int endIndex,
			List<Integer> permutationNew) {

		long betweenSize = Math.abs(startIndex -endIndex);
		for (int i = 0; i < betweenSize /2; i++) {
			
			int firstIndex = startIndex + i;
			int secondIndex = endIndex - i;
			
			int firstVaLue = permutationNew.get(firstIndex);
			int secondVaLue = permutationNew.get(secondIndex);
			
			permutationNew.set(firstIndex, secondVaLue);
			permutationNew.set(secondIndex, firstVaLue);
		}
	}
	
}
