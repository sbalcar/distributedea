package org.distributedea.problemtools.matrixfactorization.latentfactor;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problemtools.matrixfactorization.AProblemToolDifferentialEvolutionMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.operators.OperatorDifferential;

public class ProblemToolDifferentialEvolutionMF extends AProblemToolDifferentialEvolutionMF {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_DifferentialEvolution.class);
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
	public Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {

		//differential weight [0,2]
		double differentialWeightF = 0.25;
		
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

}
