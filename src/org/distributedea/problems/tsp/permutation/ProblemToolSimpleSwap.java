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
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Problem tool for TSP Problem Represent by Permutation implements Simple gene swap operator
 * @author stepan
 *
 */
public class ProblemToolSimpleSwap extends ProblemTSPPermutationTool {

	/**
	 * Improvement - swaps two genes
	 * @param individual
	 * @return
	 * @throws ProblemToolException 
	 * @throws Exception 
	 */
	@Override
	public Individual improveIndividual(Individual individual, Problem problem,
			AgentLogger logger) throws ProblemToolException {
		
		Class<?> individualClass = IndividualPermutation.class;
		Class<?> problemClass = ProblemTSP.class;
		
		boolean areParametersValid = 
				ProblemToolValidation.isIndividualTypeOf(
						individual, individualClass, logger) &&
				ProblemToolValidation.isProblemTypeOf(
						problem, problemClass, logger);
			
		if (! areParametersValid) {
			throw new ProblemToolException("Invalid parameters in Problem Tool");
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
		
		if (rndSecondIndex < rndFirstIndex) {
			int help = rndFirstIndex;
			rndFirstIndex = rndSecondIndex;
			rndSecondIndex = help;
		}
		
		convertingChunkOfPermutation(rndFirstIndex, rndSecondIndex, permutationNew);
		
		IndividualPermutation individualNew = new IndividualPermutation();
		individualNew.setPermutation(permutationNew);
		
		return individualNew;
	}

	protected void convertingChunkOfPermutation(int startIndex, int endIndex,
			List<Integer> permutationNew) {

		int firstVaLue = permutationNew.get(startIndex);
		int secondVaLue = permutationNew.get(endIndex);
		
		permutationNew.set(startIndex, secondVaLue);
		permutationNew.set(endIndex, firstVaLue);
	}
	
}
