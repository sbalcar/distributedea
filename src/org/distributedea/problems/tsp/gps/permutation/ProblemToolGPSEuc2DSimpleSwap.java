package org.distributedea.problems.tsp.gps.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.ProblemTool;
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
	public Individual generateFirstIndividual(IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) {

		DatasetTSPGPS datasetTSPGPS =  (DatasetTSPGPS) dataset;
		
		return ToolGenerateFirstIndividualTSPGPS.generateFirstIndividual(datasetTSPGPS, logger);
	}
	
	@Override
	public Individual improveIndividual(Individual individual, IProblemDefinition problemDef,
			IAgentLogger logger) throws Exception {
				
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return OperatorSimpleSwap.create(individualPerm, logger);		
	}	
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return OperatorSinglePointCrossover.crossover(ind1, ind2);
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		IndividualPermutation ind3 = (IndividualPermutation) individual2;
		
		Individual[] res1 =
				OperatorSinglePointCrossover.crossover(ind1, ind2);
		IndividualPermutation ind = (IndividualPermutation) res1[0];
		
		return OperatorSinglePointCrossover.crossover(ind, ind3);
	}

	@Override
	public Individual generateNextIndividual(IProblemDefinition problemDef,
			Dataset dataset, Individual individual, IAgentLogger logger) {
		
		IndividualPermutation individualPermutation = (IndividualPermutation) individual;
		
		return ToolNextPermutationTSPGPS.nextPermutation(individualPermutation);
	}

	@Override
	public Individual getNeighbor(Individual individual,
			IProblemDefinition problemDef, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		return generateIndividual(problemDef, dataset, logger);
	}
	
}
