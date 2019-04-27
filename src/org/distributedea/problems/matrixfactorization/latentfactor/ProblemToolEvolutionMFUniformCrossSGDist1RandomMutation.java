package org.distributedea.problems.matrixfactorization.latentfactor;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.matrixfactorization.AProblemToolEvolutionMF;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorUniformCross;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ownsgd.ToolSGDist1RandomMF;

public class ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation extends AProblemToolEvolutionMF {

	private double stepAlpha;
	
	@Deprecated
	public ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation() {
	}

	/**
	 * Constructor
	 * @param stepAlpha
	 */
	public ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation(double stepAlpha) {
		this.stepAlpha = stepAlpha;
	}

	
	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_Evolution.class);
		return agents;
	}

	@Override
	public Arguments exportArguments() {
		Arguments arguments = new Arguments();
		arguments.addArgument(new Argument("stepAlpha", stepAlpha));
		return arguments;
	}

	@Override
	public void importArguments(Arguments arguments) {
		this.stepAlpha = arguments.exportArgument("stepAlpha").exportValueAsDouble();
	}

	@Override
	public Individual mutationOfIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger)
			throws Exception {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
				
		return ToolSGDist1RandomMF.improve(idividualLF, problemMF, datasetMF, logger);		
	}
	
	@Override
	public Individual[] crossIndividual(Individual individual1,
			Individual individual2, IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualLatentFactors individualLF1 =
				(IndividualLatentFactors) individual1;
		IndividualLatentFactors individualLF2 =
				(IndividualLatentFactors) individual2;

		return OperatorUniformCross.create(individualLF1, individualLF2);
	}
	
}
