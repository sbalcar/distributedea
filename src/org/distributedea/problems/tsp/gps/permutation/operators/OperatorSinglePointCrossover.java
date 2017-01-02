package org.distributedea.problems.tsp.gps.permutation.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;

/**
 * Operator containing functionality of Single Point Crossover
 * @author stepan
 *
 */
public class OperatorSinglePointCrossover {

	public static Individual[] crossover(IndividualPermutation individual1,
			IndividualPermutation individual2) {
		
		List<Integer> perm1 = individual1.getPermutation();
		List<Integer> perm2 = individual2.getPermutation();
		
		Random rand = new Random();
		int  point = rand.nextInt(perm1.size());
		
		List<Integer> permNewStart = new ArrayList<Integer>();
		for (int geneIndex = 0; geneIndex < point; geneIndex++) {
			int geneI = perm1.get(geneIndex);
			permNewStart.add(geneI);
			if (geneIndex == point) {
				break;
			}
		}
		
		List<Integer> remainder = new ArrayList<Integer>();
		for (int geneI : perm2) {
			if (! permNewStart.contains(geneI)) {
				remainder.add(geneI);
			}
		}
		
		List<Integer> permNew1 = new ArrayList<Integer>();
		permNew1.addAll(permNewStart);
		permNew1.addAll(remainder);
		
		IndividualPermutation individualNew = new IndividualPermutation(
			permNew1);
		
		Individual[] result = new Individual[2];
		result[0] = individualNew;
		result[1] = individual1;
		
		return result;
	}
}
