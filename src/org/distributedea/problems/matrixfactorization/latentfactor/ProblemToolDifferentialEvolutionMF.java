package org.distributedea.problems.matrixfactorization.latentfactor;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.matrixfactorization.AProblemToolDifferentialEvolutionMF;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorDifferential;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorUniformCross;

public class ProblemToolDifferentialEvolutionMF extends AProblemToolDifferentialEvolutionMF {

	private double differentialWeightF;
	
	@Deprecated
	public ProblemToolDifferentialEvolutionMF() {
		super();
	}
	
	/**
	 * Constructor
	 * @param differentialWeightF  // [0,2]
	 */
	public ProblemToolDifferentialEvolutionMF(double differentialWeightF) {
		super();
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
		Arguments arguments = new Arguments();
		arguments.addArgument(new Argument("differentialWeightF", differentialWeightF));
		return arguments;
	}

	@Override
	public void importArguments(Arguments arguments) {
		this.differentialWeightF = arguments.exportArgument("differentialWeightF").exportValueAsDouble();
	}


	@Override
	public Individual differentialOfIndividuals(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualLatentFactors individualLF1 =
				(IndividualLatentFactors) individual1;
		IndividualLatentFactors individualLF2 =
				(IndividualLatentFactors) individual2;
		IndividualLatentFactors individualLF3 =
				(IndividualLatentFactors) individual3;

		ProblemMatrixFactorization problemMF =
				(ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return OperatorDifferential.create(individualLF1, individualLF2,
				individualLF3, differentialWeightF, problemMF, datasetMF, logger);
	}

	@Override
	public Individual cross(Individual individualOld, Individual individualNew,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualLatentFactors individualLFOld =
				(IndividualLatentFactors) individualOld;
		IndividualLatentFactors individualLFNew =
				(IndividualLatentFactors) individualNew;

		IndividualLatentFactors[] individualsCreated = OperatorUniformCross.create(
				individualLFOld, individualLFNew);
		
		return individualsCreated[0];
	}
	
}
