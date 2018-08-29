package org.distributedea.problemtools.matrixfactorization;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problemtools.ProblemTool;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolGenerateIndividualMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolReadSolutionMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolSGDist1ByIndexMF;

/**
 * Abstract {@link ProblemTool} for {@link ProblemMatrixFactorization} using latent factor
 * representation of {@link Individual}s
 * @author stepan
 *
 */
public abstract class AProblemToolMF extends ProblemTool {

	@Override
	public void initialization(IProblem problem, Dataset dataset,
			AgentConfiguration agentConf, IAgentLogger logger) throws Exception {
	}

	@Override
	public void exit() throws Exception {
	}

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetMF.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemMatrixFactorization.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualLatentFactors.class;
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionMF.readSolution(fileOfSolution, logger);
	}

	@Override
	public double fitness(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		IndividualLatentFactors individualLF =
				(IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF =
				(ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return ToolFitnessRMSEMF.evaluateTestingDataset(
				individualLF, problemMF, datasetMF, logger);
	}

	@Override
	protected Individual generateIndividual(IProblem problem, Dataset dataset,
			IAgentLogger logger) {
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return ToolGenerateIndividualMF.generateIndividual(problemMF, datasetMF, logger);
	}

	@Override
	protected Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return ToolGenerateIndividualMF.generateIndividual(problemMF, datasetMF, logger);
	}

	@Override
	protected Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;

		return ToolSGDist1ByIndexMF.improve(idividualLF, neighborIndex,
				problemMF, datasetMF, logger);
	}
}
