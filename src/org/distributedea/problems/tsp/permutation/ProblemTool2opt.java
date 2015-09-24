package org.distributedea.problems.tsp.permutation;

import java.util.List;

public class ProblemTool2opt extends ProblemToolSimpleSwap {

	@Override
	protected void convertingChunkOfPermutation(int startIndex, int endIndex,
			List<Integer> permutationNew) {

		int betweenSize = Math.abs(startIndex -endIndex);
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
