package org.distributedea.problems.tsp.gps.permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.problems.ProblemToolValidation;
import org.distributedea.problems.exceptions.ProblemToolException;
import org.distributedea.problems.tsp.gps.permutation.operators.SinglePointCrossover;
import org.distributedea.problems.tsp.point.permutation.ProblemTSPPointPermutationTool;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPointSimpleSwap;

/**
 * Problem tool for TSP Problem Represent by Permutation implements Simple gene swap operator
 * @author stepan
 *
 */
public class ProblemToolGPSEuc2DSimpleSwap extends ProblemTSPGPSEuc2DPermutationTool {

	
	@Override
	public Individual generateFirstIndividual(Problem problem,
			IAgentLogger logger) {

		ProblemTSP problemTSPGPS = (ProblemTSP) problem;
		
		int minValue = problemTSPGPS.minOfPositionNumbers();
		
		List<Integer> permutation = new ArrayList<Integer>();
		for (int numberI = 0; numberI <  problemTSPGPS.numberOfPositions();
				numberI++) {
			permutation.add(minValue + numberI);
		}
		
		IndividualPermutation individualPerm = new IndividualPermutation();
		individualPerm.setPermutation(permutation);
		
		return individualPerm;
	}
	
	/**
	 * Improvement - swaps two genes
	 * @param individual
	 * @return
	 * @throws ProblemToolException 
	 * @throws Exception 
	 */
	@Override
	public Individual improveIndividual(Individual individual, Problem problem,
			IAgentLogger logger) throws ProblemToolException {
				
		Class<?> individualClass = IndividualPermutation.class;
		Class<?> problemClass = problemWhichSolves();
	
		return improveIndividual(individual, problem, individualClass, problemClass, logger);
	}
	
	public Individual improveIndividual(Individual individual, Problem problem,
			Class<?> individualClass, Class<?> problemClass, IAgentLogger logger
			) throws ProblemToolException {
		
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
			Individual individual2, Problem problem, IAgentLogger logger)
			throws ProblemToolException {

		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return SinglePointCrossover.crossover(ind1, ind2);
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, Problem problem,
			IAgentLogger logger) throws ProblemToolException {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		IndividualPermutation ind3 = (IndividualPermutation) individual2;
		
		Individual[] res1 =
				SinglePointCrossover.crossover(ind1, ind2);
		IndividualPermutation ind = (IndividualPermutation) res1[0];
		
		return SinglePointCrossover.crossover(ind, ind3);
	}

	@Override
	public Individual generateNextIndividual(Problem problem,
			Individual individual, IAgentLogger logger) {
		
		ProblemTSPPointPermutationTool tool = new ProblemToolPointSimpleSwap();
		return tool.generateNextIndividual(problem, individual, logger);
	}

	@Override
	public Individual getNeighbor(Individual individual, Problem problem,
			long neighborIndex, IAgentLogger logger) throws ProblemToolException {

		return generateIndividual(problem, logger);
	}
	
}
