package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolFitnessRMSEMF {

	/**
	 * Returns fitness - Root-mean-square deviation
	 * @param individualLF
	 * @param problemMF
	 * @param datasetMF
	 * @param logger
	 * @return
	 */
	public static double evaluateTrainingDataset(IndividualLatentFactors individualLF,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
	
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
	
		double sumOfSquares = ToolFitnessSumOfSquaresMF.evaluateTraining(
				individualLF, problemMF, datasetMF, logger);
		
		int numOfRaitings = datasetModel.exportNumberOfRaitings();
		double rmse = Math.pow(sumOfSquares / numOfRaitings, 0.5);
		
		return rmse;
	}
	
	/**
	 * Returns fitness - Root-mean-square deviation
	 * @param individualLF
	 * @param problemMF
	 * @param datasetMF
	 * @param logger
	 * @return
	 */
	public static double evaluateTestingDataset(IndividualLatentFactors individualLF,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
	
		RatingModel datasetModel = datasetMF.exportTestingRatingModel();

		double sumOfSquares = ToolFitnessSumOfSquaresMF.evaluateTesting(
				individualLF, problemMF, datasetMF, logger);
		
		int numOfRaitings = datasetModel.exportNumberOfRaitings();
		double rmse = Math.pow(sumOfSquares / numOfRaitings, 0.5);
		
		return rmse;
	}

}
