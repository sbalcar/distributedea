package org.distributedea.problems.matrixfactorization.latentfactor.tools.ownsgd;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolSGDist1ByIndexMF {
	
//	static double STEP_ALPHA = 0.0002;
	static double STEP_ALPHA = 0.02;
	
	
	public static IndividualLatentFactors improve(
			IndividualLatentFactors idividualLF, long neighborIndex, double stepAlpha,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
	
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		int index = (int) (neighborIndex % datasetModel.exportNumberOfRaitings());
		
		ObjectRatingList raitings = datasetModel.exportObjectRaitingList();
		ObjectRating raiting = raitings.getRaitings().get(index);

		int rowIndex = datasetModel.exportIndexOfUser(raiting.getUserID());
		int colIndex = datasetModel.exportIndexOfItem(raiting.getItemID());
		double raitingValue = raiting.getRaiting();

		IndividualLatentFactors idividualClone =
				(IndividualLatentFactors) idividualLF.deepClone();
		
		ToolSGDist1RandomMF.improveObjectRaiting(idividualClone,
				rowIndex, colIndex, raitingValue, stepAlpha, logger);
		
		return idividualClone;
	}
}
