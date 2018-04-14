package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
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
	public static double evaluate(IndividualLatentFactors individualLF,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
	
	
		double sumOfSquares = ToolFitnessSumOfSquaresMF.evaluate(
				individualLF, problemMF, datasetMF, logger);
		
		int numOfRaitings = datasetMF.exportNumberOfRaitings();
		double rmse = Math.pow(sumOfSquares / numOfRaitings, 0.5);
		
		return rmse;
	}
}
