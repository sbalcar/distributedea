package org.distributedea.problems.tsp.point.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.gps.permutation.operators.Operator2Opt;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorDifferential;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorSinglePointCrossover;

/**
 * Represents {@link ProblemTool} for TSP {@link Problem} for permutation based
 * {@link Individual} representation. Operator implements 2opt algorithm. 
 * @author stepan
 *
 */
public class ProblemToolPoint2opt extends AProblemToolTSPPointPermutation {

	@Override
	public Individual improveIndividual(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return Operator2Opt.create(individualPerm);
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
	public Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		IndividualPermutation ind3 = (IndividualPermutation) individual3;
		
		return OperatorDifferential.create(ind1, ind2, ind3);
	}

}
