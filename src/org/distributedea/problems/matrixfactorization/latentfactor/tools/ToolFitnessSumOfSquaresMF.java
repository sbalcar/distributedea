package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaiting;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolFitnessSumOfSquaresMF {

	/**
	 * Returns fitness - Residual Sum Of Squares
	 * @param individualLF
	 * @param problemMF
	 * @param datasetMF
	 * @param logger
	 * @return
	 */
	public static double evaluate(IndividualLatentFactors individualLF,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
		
		double residualSumOfSquares = 0;
		
		for (ObjectRaiting objectRaitingI : datasetMF.getRaitings()) {
			
			int userID = objectRaitingI.getUserID();
			int itemID = objectRaitingI.getItemID();
			double raiting = objectRaitingI.getRaiting();
			
			int userIndex = datasetMF.exportIndexOfUser(userID);
			int itemIndex = datasetMF.exportIndexOfItem(itemID);
					
			double predictedRaiting =
					individualLF.exportValue(userIndex, itemIndex);
			
			residualSumOfSquares += 
					Math.pow(predictedRaiting - raiting, 2);
		}
		
		return residualSumOfSquares;
	}
}
