package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.DatasetModel;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaiting;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaitingList;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolSGDist1RandomInEachRowMF {

//	static double STEP_ALPHA = 0.0002;
	static double STEP_ALPHA = 0.02;
	
	public static IndividualLatentFactors improve(
			IndividualLatentFactors individual,
			ProblemMatrixFactorization problemMF,
			DatasetMF datasetMF, IAgentLogger logger) {
		
		DatasetModel datasetModel = datasetMF.exportTrainingDatasetModel();
		
		IndividualLatentFactors idividualClone =
				(IndividualLatentFactors) individual.deepClone();
		
		for (int userIdI : datasetModel.exportUserIDs()) {
		
			ObjectRaitingList raitingsOfUserI =
					datasetModel.exportRaitingsOfUser(userIdI);
			ObjectRaiting raitingI =
					raitingsOfUserI.exportRandomObjectRaiting();
			
			int rowIndex = datasetModel.exportIndexOfUser(raitingI.getUserID());
			int colIndex = datasetModel.exportIndexOfItem(raitingI.getItemID());
			double raitingValue = raitingI.getRaiting();
			
			ToolSGDist1RandomMF.improveObjectRaiting(idividualClone,
					rowIndex, colIndex, raitingValue, STEP_ALPHA, logger);
		}
		
		return idividualClone;
	}
	
}
