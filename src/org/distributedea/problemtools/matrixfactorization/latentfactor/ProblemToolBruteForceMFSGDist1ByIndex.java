package org.distributedea.problemtools.matrixfactorization.latentfactor;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problemtools.matrixfactorization.AProblemToolBruteForceMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolGenerateIndividualMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolSGDist1ByIndexMF;

public class ProblemToolBruteForceMFSGDist1ByIndex extends AProblemToolBruteForceMF {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_BruteForce.class);
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
	public Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return ToolGenerateIndividualMF.generateIndividual(problemMF, datasetMF, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;

		return ToolSGDist1ByIndexMF.improve(idividualLF, neighborIndex,
				problemMF, datasetMF, logger);
	}

}
