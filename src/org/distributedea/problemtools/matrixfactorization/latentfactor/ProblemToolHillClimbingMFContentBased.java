package org.distributedea.problemtools.matrixfactorization.latentfactor;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problemtools.matrixfactorization.AProblemToolHillClimbingMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.operators.OperatorAdulterateOfLatentVectorBySameGenreLatentVector;

public class ProblemToolHillClimbingMFContentBased extends AProblemToolHillClimbingMF {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_HillClimbing.class);
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
	public Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return OperatorAdulterateOfLatentVectorBySameGenreLatentVector
				.improve(idividualLF, problemMF, datasetMF, logger);
	}
	
}
