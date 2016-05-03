package org.distributedea.problems.tsp.point.permutation;

import java.util.ArrayList;
import java.util.List;

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
import org.distributedea.problems.tsp.gps.permutation.operators.SinglePointCrossover;
import org.distributedea.problems.tsp.point.ProblemTSPPointTool;
import org.distributedea.problems.tsp.point.permutation.tools.PermutationTool;

public abstract class ProblemTSPPointPermutationTool extends ProblemTSPPointTool implements IProblemTSPPermutationTool {

	@Override
	public Class<?> reprezentationWhichUses() {

		return IndividualPermutation.class;
	}
	
	@Override
	public Individual generateFirstIndividual(Problem problem,
			AgentLogger logger) {
		
		ProblemToolGPSEuc2DSimpleSwap tool = new ProblemToolGPSEuc2DSimpleSwap();
		return tool.generateFirstIndividual(problem, logger);
	}
	
	@Override
	public Individual generateIndividual(Problem problem, AgentLogger logger) {
		
		ProblemTSPPoint problemTSP = (ProblemTSPPoint) problem;
		
		List<Position> positions = new ArrayList<Position>();
		for (PositionPoint positionI : problemTSP.getPositions()) {
			positions.add(positionI);
		}
		
		ProblemToolGPSEuc2DSimpleSwap tool = new ProblemToolGPSEuc2DSimpleSwap();
		return tool.generateIndividual(positions);
	}
	
	@Override
	public Individual generateNextIndividual(Problem problem,
			Individual individual, AgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		List<Integer> perm = individualPerm.getPermutation();
		
		List<Integer> permNew = PermutationTool.nextPermutation(perm);
		
		IndividualPermutation individualPermNew = new IndividualPermutation();
		individualPermNew.setPermutation(permNew);
		
		return individualPermNew;
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
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return SinglePointCrossover.crossover(ind1, ind2);
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, Problem problem,
			AgentLogger logger) throws ProblemToolException {
		
		IndividualPermutation ind1 = (IndividualPermutation) individual1;
		IndividualPermutation ind2 = (IndividualPermutation) individual2;
		
		return SinglePointCrossover.crossover(ind1, ind2);
	}
	
	@Override
	public Individual getNeighbor(Individual individual, Problem problem,
			long neighborIndex, AgentLogger logger) throws ProblemToolException {
		
		ProblemToolGPSEuc2DSimpleSwap tool = new ProblemToolGPSEuc2DSimpleSwap();
		return tool.generateIndividual(problem, logger);
	}
	
}
