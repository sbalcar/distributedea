package org.distributedea.problems.matrixfactorization.latentfactor.tools.ownsgd;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolSGDist1RandomInEachRowMF {
	
	public static IndividualLatentFactors improve(
			IndividualLatentFactors individual, double stepAlpha,
			ProblemMatrixFactorization problemMF,
			DatasetMF datasetMF, IAgentLogger logger) {
		
		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		IndividualLatentFactors idividualClone =
				(IndividualLatentFactors) individual.deepClone();
		
		for (int userIdI : datasetModel.exportUserIDs()) {
		
			ObjectRatingList raitingsOfUserI =
					datasetModel.exportRaitingsOfUser(userIdI);
			ObjectRating raitingI =
					raitingsOfUserI.exportRandomObjectRaiting();
			
			int rowIndex = datasetModel.exportIndexOfUser(raitingI.getUserID());
			int colIndex = datasetModel.exportIndexOfItem(raitingI.getItemID());
			double raitingValue = raitingI.getRaiting();
			
			ToolSGDist1RandomMF.improveObjectRaiting(idividualClone,
					rowIndex, colIndex, raitingValue, stepAlpha, logger);
		}
		
		return idividualClone;
	}
	
}
