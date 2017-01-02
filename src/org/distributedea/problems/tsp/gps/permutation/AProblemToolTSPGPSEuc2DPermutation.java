package org.distributedea.problems.tsp.gps.permutation;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionGPS;
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
			Problem problem, IAgentLogger logger) {
		
		ProblemTSPGPS problemTSP = (ProblemTSPGPS) problem;
		
		return ToolGenerateIndividualTSPGPS.generateIndividual(problemTSP, logger);
	}
	
	@Override
	public Problem readProblem(File problemFile, IAgentLogger logger) {

		return ToolReadProblemTSPGPS.readProblem(problemFile, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Problem problem,
			IAgentLogger logger) {

		return ToolReadSolutionTSPGPS.readSolution(fileOfSolution, logger);
	}
	
	@Override
	public double fitness(Individual individual, IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		ProblemTSPGPS problemTSP = (ProblemTSPGPS) problem;

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
