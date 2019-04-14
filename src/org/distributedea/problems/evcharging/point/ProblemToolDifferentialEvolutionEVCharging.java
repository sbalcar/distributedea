package org.distributedea.problems.evcharging.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorDifferentialAllDimensions;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorOnePointCrossover;
import org.distributedea.problems.evcharging.AProblemToolDifferentialEvolutionEVCharging;

public class ProblemToolDifferentialEvolutionEVCharging extends AProblemToolDifferentialEvolutionEVCharging {

	private double differentialWeightF;
	
	@Deprecated
	public ProblemToolDifferentialEvolutionEVCharging() {
		super();
	}
	
	/**
	 * Constructor
	 * @param normalDistMultiplicator
	 * @param differentialWeightF  // [0,2]
	 */
	public ProblemToolDifferentialEvolutionEVCharging(double normalDistMultiplicator, double differentialWeightF) {
		super(normalDistMultiplicator);
		this.differentialWeightF = differentialWeightF;
	}
	
	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_DifferentialEvolution.class);
		return agents;
	}

	@Override
	public Arguments exportArguments() {
		Arguments arguments = super.exportArguments();
		arguments.addArgument(new Argument("differentialWeightF", differentialWeightF));
		return arguments;
	}

	@Override
	public void importArguments(Arguments arguments) {
		super.importArguments(arguments);
		this.differentialWeightF = arguments.exportArgument("differentialWeightF").exportValueAsDouble();
	}

	@Override
	public Individual differentialOfIndividuals(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;
		IndividualPoint individualP3 = (IndividualPoint) individual3;
		
		return OperatorDifferentialAllDimensions.create(individualP1,
				individualP2, individualP3, differentialWeightF, logger);
	}

	@Override
	public Individual cross(Individual individualOld, Individual individualNew,
			IProblem problem, Dataset dataset, IAgentLogger logger
			) throws Exception {
		
		IndividualPoint individualPointOld = (IndividualPoint) individualOld;
		IndividualPoint individualPointNew = (IndividualPoint) individualNew;

//		Individual[] idividuals = OperatorMoveToSomewhereInTheMiddle.create(
		Individual[] idividuals = OperatorOnePointCrossover.create(
				individualPointOld, individualPointNew, logger);
		
		return idividuals[0];
	}
	
}
