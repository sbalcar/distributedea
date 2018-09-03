package org.distributedea.problemtools.matrixfactorization.latentfactor;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problemtools.matrixfactorization.AProblemToolEvolutionMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.operators.OperatorUniformCross;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolSGDist1RandomMF;

public class ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation extends AProblemToolEvolutionMF {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_Evolution.class);
		return agents;
	}

	@Override
	public Arguments exportArguments() {
		return new Arguments();
	}

	@Override
	public void importArguments(Arguments arguments) {
	}

	@Override
	public Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger)
			throws Exception {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
				
		return ToolSGDist1RandomMF.improve(idividualLF, problemMF, datasetMF, logger);		
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualLatentFactors individualLF1 =
				(IndividualLatentFactors) individual1;
		IndividualLatentFactors individualLF2 =
				(IndividualLatentFactors) individual2;

		return OperatorUniformCross.create(individualLF1, individualLF2);
	}
	
}
