package org.distributedea.problems.tsp.point.permutation;

import java.io.File;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionPoint;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.gps.permutation.IProblemTSPPermutationTool;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolNextPermutationTSPGPS;
import org.distributedea.problems.tsp.point.ProblemTSPPointTool;
import org.distributedea.problems.tsp.point.permutation.tools.ToolDistanceTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolFitnessTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolGenerateIndividualForTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolReadProblemTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolReadSolutionTSPPoints;


/**
 * Abstract {@link ProblemTool} for {@link ProblemTSPPoint} using permutation
 * representation of {@link Individual}s
 * @author stepan
 *
 */
public abstract class AProblemToolTSPPointPermutation extends ProblemTSPPointTool implements IProblemTSPPermutationTool {

	@Override
	public Class<?> problemWhichSolves() {

		return ProblemTSPPoint.class;
	}
	
	@Override
	public Class<?> reprezentationWhichUses() {

		return IndividualPermutation.class;
	}
	
	@Override
	public Individual generateFirstIndividual(IProblemDefinition probleDef,
			Problem problem, IAgentLogger logger) {
		
		ProblemTSPPoint problemTSPPoint = (ProblemTSPPoint)problem;
		
		return ToolGenerateIndividualForTSPPoint.generate(problemTSPPoint);
	}
	
	@Override
	public Individual generateIndividual(IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) {
		
		ProblemTSPPoint problemTSPPoint = (ProblemTSPPoint) problem;
		
		return ToolGenerateIndividualForTSPPoint.generate(problemTSPPoint);

	}
	
	@Override
	public Individual generateNextIndividual(IProblemDefinition problemDef,
			Problem problem, Individual individual, IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		List<Integer> perm = individualPerm.getPermutation();
		
		List<Integer> permNew = ToolNextPermutationTSPGPS.nextPermutation(perm);
		
		return new IndividualPermutation(permNew);
	}
	
	@Override
	public double fitness(Individual individual, IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) {
		
		IndividualPermutation individualPermutation =
				(IndividualPermutation) individual;
		ProblemTSPPoint problemTSPPoint = (ProblemTSPPoint) problem;
		
		return ToolFitnessTSPPoint.fitness(individualPermutation, problemTSPPoint, this, logger);
	}
	
	@Override
	public Problem readProblem(File problemFile, IAgentLogger logger) {

		return ToolReadProblemTSPPoint.readProblem(problemFile, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Problem problem,
			IAgentLogger logger) {
		
		return ToolReadSolutionTSPPoints.readSolutionTSP(fileOfSolution, logger);		
	}
	
	@Override
	public Individual getNeighbor(Individual individual,
			IProblemDefinition problemDef, Problem problem,
			long neighborIndex, IAgentLogger logger) throws Exception {
		
		ProblemTSPPoint problemTSPPoint = (ProblemTSPPoint) problem;
		
		return ToolGenerateIndividualForTSPPoint.generate(problemTSPPoint);
	}
	
	@Override
	public double distanceBetween(Position position1, Position position2,
			IAgentLogger logger) {
		
		PositionPoint positionPoint1 = (PositionPoint) position1;
		PositionPoint positionPoint2 = (PositionPoint) position2;
		
		return ToolDistanceTSPPoint.distanceBetween(positionPoint1, positionPoint2, logger);
	}
	
}
