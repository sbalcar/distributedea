package org.distributedea.problems.evcharging.point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.evcharging.AProblemToolBruteForceEVCharging;
import org.distributedea.problems.evcharging.point.tools.ToolGenerateNextIndividualEVCharging;

public class ProblemToolBruteForceEVCharging extends AProblemToolBruteForceEVCharging {

	private double maxStep;

	private Random randomn;
	
	@Deprecated
	public ProblemToolBruteForceEVCharging() {
		super();
		this.randomn = new Random();
	}
	
	/**
	 * Constructor
	 * @param normalDistMultiplicator
	 * @param maxStep
	 */
	public ProblemToolBruteForceEVCharging(double normalDistMultiplicator, double maxStep) {
		super(normalDistMultiplicator);
		this.maxStep = maxStep;
	}
	
	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_BruteForce.class);
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
	public Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		return generateIndividual(problem, dataset, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem, Dataset dataset,
			Individual individual, long neighborIndex, IAgentLogger logger) {
		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return ToolGenerateNextIndividualEVCharging.create(individualPoint,
				-1, 1, maxStep, randomn, logger);
	}
	
}
