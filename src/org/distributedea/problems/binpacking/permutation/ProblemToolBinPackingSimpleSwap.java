package org.distributedea.problems.binpacking.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorSimpleSwap;

public class ProblemToolBinPackingSimpleSwap extends ProblemToolBinPackingSimpleShift {

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblem problem, IAgentLogger logger) throws Exception {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleSwap.create(individualPerm, logger);
	}
	
	@Override
	protected Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleSwap.create(individualPerm, logger);
	}
}
