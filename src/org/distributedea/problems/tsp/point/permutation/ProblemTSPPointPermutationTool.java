package org.distributedea.problems.tsp.point.permutation;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionPoint;
import org.distributedea.problems.exceptions.ProblemToolException;
import org.distributedea.problems.tsp.gps.permutation.IProblemTSPPermutationTool;
import org.distributedea.problems.tsp.gps.permutation.ProblemTSPGPSEuc2DPermutationTool;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2DSimpleSwap;
import org.distributedea.problems.tsp.point.ProblemTSPPointTool;

public abstract class ProblemTSPPointPermutationTool extends ProblemTSPPointTool implements IProblemTSPPermutationTool {

	@Override
	public Class<?> reprezentationWhichUses() {

		return IndividualPermutation.class;
	}
	
	@Override
	public Individual generateIndividual(Problem problem, AgentLogger logger) {
		
		ProblemToolGPSEuc2DSimpleSwap tool = new ProblemToolGPSEuc2DSimpleSwap();
		return tool.generateIndividual(problem, logger);
	}
	
	@Override
	public double fitness(Individual individual, Problem problem,
			AgentLogger logger) {
		
		IndividualPermutation individualPermutation =
				(IndividualPermutation) individual;
		ProblemTSPPoint problemTSPGPS =
				(ProblemTSPPoint) problem;
		
		return ProblemTSPGPSEuc2DPermutationTool.fitnessPermutation(
				individualPermutation, problemTSPGPS, this, logger);
		
	}
	
	@Override
	public double distanceBetween(Position position1, Position position2,
			AgentLogger logger) {
		
		PositionPoint positionPoint1 = (PositionPoint) position1;
		PositionPoint positionPoint2 = (PositionPoint) position2;
		
		double deltaX = Math.abs(
				positionPoint1.getCoordinateX() -
				positionPoint2.getCoordinateX() );
		double deltaY = Math.abs(
				positionPoint1.getCoordinateY() -
				positionPoint2.getCoordinateY() );
		
		double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		return Math.round(distance);
	}
	
	@Override
	public Individual readSolution(String fileName, Problem problem,
			AgentLogger logger) {
		
		ProblemToolGPSEuc2DSimpleSwap tool = new ProblemToolGPSEuc2DSimpleSwap();
		return tool.readSolution(fileName, problem, logger);
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Problem problem, AgentLogger logger)
			throws ProblemToolException {
		
		throw new ProblemToolException("Not possible to implement in this context");
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			Individual individual4, Problem problem, AgentLogger logger)
			throws ProblemToolException {
		
		throw new ProblemToolException("Not possible to implement in this context");
	}
	
}
