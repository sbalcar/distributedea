package org.distributedea.problems.evcharging.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorRandomMove;
import org.distributedea.problems.evcharging.AProblemToolHillClimbingEVCharging;

public class ProblemToolHillClimbingEVChargingRandomMove extends AProblemToolHillClimbingEVCharging {

	private double maxStep;

	@Deprecated
	public ProblemToolHillClimbingEVChargingRandomMove() {
		super();
	}
	
	/**
	 * Constructor
	 * @param normalDistMultiplicator
	 * @param maxStep
	 */
	public ProblemToolHillClimbingEVChargingRandomMove(double normalDistMultiplicator, double maxStep) {
		super(normalDistMultiplicator);
		this.maxStep = maxStep;
	}
	
	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_HillClimbing.class);
		return agents;
	}

	@Override
	public Arguments exportArguments() {
		Arguments arguments = super.exportArguments();
		arguments.addArgument(new Argument("maxStep", maxStep));
		return arguments;
	}

	@Override
	public void importArguments(Arguments arguments) {
		super.importArguments(arguments);
		this.maxStep = arguments.exportArgument("maxStep").exportValueAsDouble();
	}
	
	@Override
	public Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, maxStep, logger);
	}

}
