package org.distributedea.problemtools.tsp.point.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.ProblemTool;
import org.distributedea.problemtools.tsp.gps.permutation.operators.OperatorDifferential;
import org.distributedea.problemtools.tsp.gps.permutation.operators.OperatorSimpleSwap;
import org.distributedea.problemtools.tsp.gps.permutation.operators.OperatorSinglePointCrossover;

/**
 * Represents {@link ProblemTool} for TSP Point {@link Problem} for permutation based
 * {@link Individual} representation. Operator implements simple gene swap algorithm. 
 * @author stepan
 *
 */
public class ProblemToolPointSimpleSwap extends AProblemToolTSPPointPermutation {
	
	@Override
	public Arguments exportArguments() {
		return new Arguments();
	}

	@Override
	public void importArguments(Arguments arguments) {
		if (arguments == null || ! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
	}
	
	@Override
	public Individual improveIndividual(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleSwap.create(individualPerm, logger);		
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
