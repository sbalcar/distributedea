package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolFitnessSumOfSquaresMF {

	/**
	 * Returns training fitness - Residual Sum Of Squares
	 * @param individualLF
	 * @param problemMF
	 * @param datasetMF
	 * @param logger
	 * @return
	 */
	public static double evaluateTraining(IndividualLatentFactors individualLF,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
		
		RatingModel testingModel = datasetMF.exportTrainingRatingModel();
		
		ObjectRatingList testingObjects = testingModel.exportObjectRaitingList();
		
		
		double residualSumOfSquares = 0;
		
		for (ObjectRating objectRaitingI : testingObjects.getRaitings()) {
			
			int userID = objectRaitingI.getUserID();
			int itemID = objectRaitingI.getItemID();
			double raiting = objectRaitingI.getRaiting();
			
			int userIndex = testingModel.exportIndexOfUser(userID);
			int itemIndex = testingModel.exportIndexOfItem(itemID);
					
			double predictedRaiting =
					individualLF.exportValue(userIndex, itemIndex);
			
			residualSumOfSquares += 
					Math.pow(predictedRaiting - raiting, 2);
		}
		
		return residualSumOfSquares;
	}

	/**
	 * Returns testing fitness - Residual Sum Of Squares
	 * @param individualLF
	 * @param problemMF
	 * @param datasetMF
	 * @param logger
	 * @return
	 */
	public static double evaluateTesting(IndividualLatentFactors individualLF,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
		
		RatingModel testingModel = datasetMF.exportTestingRatingModel();
		
		ObjectRatingList trainingObjects = testingModel.exportObjectRaitingList();
		
		
		double residualSumOfSquares = 0;
		
		for (ObjectRating objectRaitingI : trainingObjects.getRaitings()) {
			
			int userID = objectRaitingI.getUserID();
			int itemID = objectRaitingI.getItemID();
			double raiting = objectRaitingI.getRaiting();
			
			int userIndex = testingModel.exportIndexOfUser(userID);
			int itemIndex = testingModel.exportIndexOfItem(itemID);
					
			double predictedRaiting =
					individualLF.exportValue(userIndex, itemIndex);
			
			residualSumOfSquares += 
					Math.pow(predictedRaiting - raiting, 2);
		}
		
		return residualSumOfSquares;
	}

}
