package org.distributedea.problems.tsp.permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.problems.ProblemToolValidation;


public class ProblemToolSimpleSwap extends ProblemTSPPermutationTool {

	/**
	 * Improvement - swaps two genes
	 * @param individual
	 * @return
	 */
	@Override
	public Individual improveIndividual(Individual individual, Problem problem,
			AgentLogger logger) {
		
		Class<?> individualClass = IndividualPermutation.class;
		Class<?> problemClass = ProblemTSP.class;
		
		boolean areParametersValid = 
				ProblemToolValidation.isIndividualTypeOf(
						individual, individualClass, logger) &&
				ProblemToolValidation.isProblemTypeOf(
						problem, problemClass, logger);
			
		if (! areParametersValid) {
			//:TODO - ukonci agenta
		}
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
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
		
		IndividualPermutation individualNew = new IndividualPermutation();
		individualNew.setPermutation(permutationNew);
		
		return individualNew;
	}
	
}
