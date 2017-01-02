package org.distributedea.problems.binpacking.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorSimpleSwap;

public class ProblemToolBinPackingSimpleSwap extends ProblemToolBinPackingSimpleShift {

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblemDefinition problemDef, IAgentLogger logger) throws Exception {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleSwap.create(individualPerm, logger);
	}
	
	@Override
	protected Individual getNeighbor(Individual individual,
			IProblemDefinition problemDef, Problem problem,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleSwap.create(individualPerm, logger);
	}
}
