package org.distributedea.problems.tsp.gps.permutation.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolNextPermutationTSPGPS {

	public static IndividualPermutation nextPermutation(IndividualPermutation individual) {
		
		List<Integer> permutation = individual.getPermutation();
		
		List<Integer> newPermutation = nextPermutation(permutation);
		
		return new IndividualPermutation(newPermutation);
	}
	
	public static List<Integer> nextPermutation(List<Integer> permutation) {
	       
        // find longest non-increasing suffix
        int suffixIndexI = permutation.size() -1;
        while (true) {
           
            // last permutation = next doesn't exist
            if (suffixIndexI == 0) {
                return null;
            }
           
            int valueI = permutation.get(suffixIndexI);
            int valueImin1 = permutation.get(suffixIndexI -1);
           
            if (valueImin1 >= valueI) {
                suffixIndexI--;
            } else {
                break;
            }
        }
       
        // identify pivot
        int pivotIndex = suffixIndexI -1;
        int pivot = permutation.get(pivotIndex);
       
        // element to swap
        int elementToSwapIndexI = permutation.size() -1;
        while (elementToSwapIndexI >= suffixIndexI) {
           
            int valueI = permutation.get(elementToSwapIndexI);
           
            if (pivot < valueI) {
                break;
            } else {
                elementToSwapIndexI--;
            }
        }
        int elementToSwap = permutation.get(elementToSwapIndexI);
       
        // new permutation       
        List<Integer> newPermutation = new ArrayList<Integer>();
        for (int i = 0; i < pivotIndex; i++) {
            int valueI = permutation.get(i);
            newPermutation.add(valueI);
        }
       
        // swap with pivot
        newPermutation.add(elementToSwap);
       
        // reverse the suffix
        for (int i = permutation.size() -1; i > pivotIndex; i--) {
           
            if (i == elementToSwapIndexI) {
                newPermutation.add(pivot);
            } else {
                int valI = permutation.get(i);
                newPermutation.add(valI);
            }
        }
       
        return newPermutation;
    }
	
}
