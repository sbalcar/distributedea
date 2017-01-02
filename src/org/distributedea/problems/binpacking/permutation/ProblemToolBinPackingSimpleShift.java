package org.distributedea.problems.binpacking.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.binpacking.permutation.operators.OperatorSimpleShift;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorCrossPermutation;


public class ProblemToolBinPackingSimpleShift extends AProblemToolBinPackingPermutation {

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblemDefinition problemDef, IAgentLogger logger) throws Exception {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleShift.create(individualPerm, logger);
	}

	@Override
	protected Individual getNeighbor(Individual individual, IProblemDefinition problemDef,
			Problem problem, long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleShift.create(individualPerm, logger);
	}

	@Override
	protected Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) throws Exception {

		IndividualPermutation individualPerm1 = (IndividualPermutation) individual1;
		IndividualPermutation individualPerm2 = (IndividualPermutation) individual2;
		
		Individual newIndiv = OperatorCrossPermutation.crossover(individualPerm1, individualPerm2);
		
		Individual [] result = new Individual[2];
		result[0] = newIndiv;
		result[1] = individual1;

		return result;
	}

	@Override
	protected Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) throws Exception {
		
		return createNewIndividual(individual1, individual2, problemDef, problem, logger);
	}

}
