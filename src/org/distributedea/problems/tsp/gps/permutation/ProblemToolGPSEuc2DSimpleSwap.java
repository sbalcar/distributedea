package org.distributedea.problems.tsp.gps.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorDifferential;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorSimpleSwap;
import org.distributedea.problems.tsp.gps.permutation.operators.OperatorSinglePointCrossover;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolGenerateFirstIndividualTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolNextPermutationTSPGPS;

/**
 * Represents {@link ProblemTool} for TSP {@link Problem} for permutation based
 * {@link Individual} representation. Operator implements simple gene swap algorithm. 
 * @author stepan
 *
 */
public class ProblemToolGPSEuc2DSimpleSwap extends AProblemToolTSPGPSEuc2DPermutation {

	
	@Override
	public Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		DatasetTSPGPS datasetTSPGPS =  (DatasetTSPGPS) dataset;
		
		return ToolGenerateFirstIndividualTSPGPS.generateFirstIndividual(datasetTSPGPS, logger);
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
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		IndividualPermutation ind3 = (IndividualPermutation) individual2;
		
		return OperatorDifferential.create(ind1, ind2, ind3);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {
		
		IndividualPermutation individualPermutation = (IndividualPermutation) individual;
		
		return ToolNextPermutationTSPGPS.nextPermutation(individualPermutation);
	}

	@Override
	public Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		return generateIndividual(problem, dataset, logger);
	}
	
}
