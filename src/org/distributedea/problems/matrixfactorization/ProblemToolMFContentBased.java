package org.distributedea.problems.matrixfactorization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorAdulterateOfLatentVectorBySameGenreLatentVector;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorDifferential;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorUniformCross;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;

/**
 * Represents {@link ProblemTool} for Matrix Factorization {@link Problem} for latent factors based
 * {@link Individual} representation. Operator takes one randomly selected pair of movies with the
 * equal genre. the latent vector of the second individual * ALPHA scalar is added to the latent
 * vector of the first individual.
 * @author stepan
 *
 */
public class ProblemToolMFContentBased extends AProblemToolMF {

	@Override
	public Dataset readDataset(IDatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {

		DatasetDescriptionMF datasetDescr =
				(DatasetDescriptionMF)datasetDescription;
		ProblemMatrixFactorization problemMF =
				(ProblemMatrixFactorization) problem;
		
		return ToolReadDatasetMF.readDatasetWithContent(
				datasetDescr, problemMF, logger);
	}

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger)
			throws Exception {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return OperatorAdulterateOfLatentVectorBySameGenreLatentVector
				.improve(idividualLF, problemMF, datasetMF, logger);
	}

	@Override
	protected Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {
		
		IndividualLatentFactors idividualLF = (IndividualLatentFactors) individual;
		
		ProblemMatrixFactorization problemMF = (ProblemMatrixFactorization) problem;
		DatasetMF datasetMF = (DatasetMF) dataset;
		
		return OperatorAdulterateOfLatentVectorBySameGenreLatentVector
				.improve(idividualLF, problemMF, datasetMF, logger);
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
