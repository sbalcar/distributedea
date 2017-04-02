package org.distributedea.problems.tsp.gps.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.tsp.gps.permutation.operators.Operator2Opt;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorTwoPointCrossoverPermutation;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorDifferential;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolGenerateFirstIndividualTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolNextPermutationTSPGPS;

public class ProblemToolGPSEuc2D2opt extends AProblemToolTSPGPSEuc2DPermutation {

	@Override
	protected Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		DatasetTSPGPS datasetTSPGPS =  (DatasetTSPGPS) dataset;
		
		return ToolGenerateFirstIndividualTSPGPS.generateFirstIndividual(datasetTSPGPS, logger);
	}

	@Override
	protected Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return ToolNextPermutationTSPGPS.nextPermutation(individualPerm);
	}

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualPermutation indivPerm = (IndividualPermutation) individual;
		
		return Operator2Opt.create(indivPerm);
	}

	@Override
	protected Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPermutation indivPerm = (IndividualPermutation) individual;
		
		return Operator2Opt.create(indivPerm);
	}

	@Override
	protected Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem,
			Dataset dataset, IAgentLogger logger)throws Exception {

		IndividualPermutation individualPerm1 = (IndividualPermutation) individual1;
		IndividualPermutation individualPerm2 = (IndividualPermutation) individual2;
		
		Individual newIndiv = OperatorTwoPointCrossoverPermutation.crossover(individualPerm1, individualPerm2);
		
		Individual [] result = new Individual[2];
		result[0] = newIndiv;
		result[1] = individual1;

		return result;
	}

	@Override
	protected Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualPermutation individualPerm1 = (IndividualPermutation) individual1;
		IndividualPermutation individualPerm2 = (IndividualPermutation) individual2;
		IndividualPermutation individualPerm3 = (IndividualPermutation) individual3;

		return OperatorDifferential.create(individualPerm1, individualPerm2, individualPerm3);

	}

}
