package org.distributedea.problems.tsp.gps.permutation;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionGPS;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.gps.ProblemTSPGPSTool;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolDistanceTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolFitnessTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolGenerateIndividualTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolReadProblemTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolReadSolutionTSPGPS;

/**
 * Abstract {@link ProblemTool} for {@link ProblemTSPPoint} using permutation
 * representation of {@link Individual}s
 * @author stepan
 *
 */
public abstract class AProblemToolTSPGPSEuc2DPermutation extends ProblemTSPGPSTool implements IProblemTSPPermutationTool {
	
	@Override
	public Class<?> reprezentationWhichUses() {
	
		return IndividualPermutation.class;
	}
	
	@Override
	public Individual generateIndividual(IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) {
		
		DatasetTSPGPS problemTSP = (DatasetTSPGPS) dataset;
		
		return ToolGenerateIndividualTSPGPS.generateIndividual(problemTSP, logger);
	}
	
	@Override
	public Dataset readDataset(File problemFile, IAgentLogger logger) {

		return ToolReadProblemTSPGPS.readDataset(problemFile, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {

		return ToolReadSolutionTSPGPS.readSolution(fileOfSolution, logger);
	}
	
	@Override
	public double fitness(Individual individual, IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		DatasetTSPGPS problemTSP = (DatasetTSPGPS) dataset;

	    return ToolFitnessTSPGPS.evaluate(individualPerm, problemTSP, this, logger);
	}

	@Override
	public double distanceBetween(Position position1,
			Position position2, IAgentLogger logger)  {
		
		PositionGPS positionGPS1 = (PositionGPS) position1;
		PositionGPS positionGPS2 = (PositionGPS) position2;
		
		return ToolDistanceTSPGPS.distanceBetween(positionGPS1, positionGPS2, logger);
	}
	
}
