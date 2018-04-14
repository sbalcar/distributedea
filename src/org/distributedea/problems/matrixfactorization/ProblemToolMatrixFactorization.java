package org.distributedea.problems.matrixfactorization;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorDifferential;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorUniformCross;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolGenerateIndividualMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolReadSolutionMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolSGDist1ByIndexMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolSGDist1RandomInEachRowMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolSGDist1RandomMF;


public  class ProblemToolMatrixFactorization extends ProblemTool {

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
	public Dataset readDataset(File datasetFile, IAgentLogger logger) {
		
		return ToolReadDatasetMF.readDataset(datasetFile, logger);
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
		
		return ToolFitnessRMSEMF.evaluate(individualLF, problemMF, datasetMF, logger);
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

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger)
			throws Exception {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
				
		return ToolSGDist1RandomMF.improve(idividualLF, problemMF, datasetMF, logger);		
	}

	@Override
	protected Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {

		IndividualLatentFactors individualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return ToolSGDist1RandomInEachRowMF.improve(individualLF, problemMF,
				datasetMF, logger);		
	}

	@Override
	protected Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualLatentFactors individualLF1 =
				(IndividualLatentFactors) individual1;
		IndividualLatentFactors individualLF2 =
				(IndividualLatentFactors) individual2;

		return OperatorUniformCross.create(individualLF1, individualLF2);
	}

	@Override
	protected Individual createNewIndividual(Individual individual1,
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
