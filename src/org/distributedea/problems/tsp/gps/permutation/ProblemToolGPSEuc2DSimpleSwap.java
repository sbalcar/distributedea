package org.distributedea.problems.tsp.gps.permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;

/**
 * Problem tool for TSP Problem Represent by Permutation implements Simple gene swap operator
 * @author stepan
 *
 */
public class ProblemToolGPSEuc2DSimpleSwap extends ProblemTSPGPSEuc2DPermutationTool {

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
		Class<?> problemClass = ProblemTSPGPS.class;
		
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
	
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Problem problem, AgentLogger logger)
			throws ProblemToolException {

		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return singlePointCrossover(ind1, ind2);
	}
	
	private Individual[] singlePointCrossover(IndividualPermutation individual1,
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
		
		IndividualPermutation individualNew = new IndividualPermutation();
		individualNew.setPermutation(permNew1);
		
		Individual[] result = new Individual[2];
		result[0] = individualNew;
		result[1] = individual1;
		
		return result;
	}
	
}
