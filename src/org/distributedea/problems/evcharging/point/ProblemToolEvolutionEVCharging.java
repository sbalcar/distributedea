package org.distributedea.problems.evcharging.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorMoveToSomewhereInTheMiddle;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorRandomMove;
import org.distributedea.problems.evcharging.AProblemToolEvolutionEVCharging;

public class ProblemToolEvolutionEVCharging extends AProblemToolEvolutionEVCharging {

	private double maxStep;

	@Deprecated
	public ProblemToolEvolutionEVCharging() {
		super();
	}
	
	/**
	 * Constructor
	 * @param normalDistMultiplicator
	 * @param maxStep
	 */
	public ProblemToolEvolutionEVCharging(double normalDistMultiplicator, double maxStep) {
		super(normalDistMultiplicator);
		this.maxStep = maxStep;
	}
	
	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_Evolution.class);
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
	public Individual mutationOfIndividual(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, maxStep, logger);
	}

	
	@Override
	public Individual[] crossIndividual(Individual individual1, Individual individual2, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;

		Individual[] idividuals = OperatorMoveToSomewhereInTheMiddle.create(
				individualP1, individualP2, logger);
		
		Individual[] result = new Individual[1];
		result[0] = idividuals[0];
		
		return result;
	}
	
}
