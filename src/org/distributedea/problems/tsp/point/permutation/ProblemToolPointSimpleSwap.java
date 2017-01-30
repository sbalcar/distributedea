package org.distributedea.problems.tsp.point.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2DSimpleSwap;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorSinglePointCrossover;

/**
 * Represents {@link ProblemTool} for TSP Point {@link Problem} for permutation based
 * {@link Individual} representation. Operator implements simple gene swap algorithm. 
 * @author stepan
 *
 */
public class ProblemToolPointSimpleSwap extends AProblemToolTSPPointPermutation {
	
	@Override
	public Individual improveIndividual(Individual individual, IProblem problem,
			IAgentLogger logger) throws Exception {
		
		ProblemToolGPSEuc2DSimpleSwap tool = new ProblemToolGPSEuc2DSimpleSwap();
		return tool.improveIndividual(individual, problem, logger);
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return OperatorSinglePointCrossover.crossover(ind1, ind2);
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return OperatorSinglePointCrossover.crossover(ind1, ind2);
	}

}
