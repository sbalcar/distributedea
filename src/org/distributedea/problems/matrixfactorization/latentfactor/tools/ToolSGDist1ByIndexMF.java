package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaiting;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaitingList;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolSGDist1ByIndexMF {
	
//	static double STEP_ALPHA = 0.0002;
	static double STEP_ALPHA = 0.02;
	
	
	public static IndividualLatentFactors improve(
			IndividualLatentFactors idividualLF, long neighborIndex,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) {
	
		int index = (int) (neighborIndex % datasetMF.exportNumberOfRaitings());
		
		ObjectRaitingList raitings = datasetMF.exportObjectRaitingList();
		ObjectRaiting raiting = raitings.getRaitings().get(index);

		int rowIndex = datasetMF.exportIndexOfUser(raiting.getUserID());
		int colIndex = datasetMF.exportIndexOfItem(raiting.getItemID());
		double raitingValue = raiting.getRaiting();

		IndividualLatentFactors idividualClone =
				(IndividualLatentFactors) idividualLF.deepClone();
		
		ToolSGDist1RandomMF.improveObjectRaiting(idividualClone,
				rowIndex, colIndex, raitingValue, STEP_ALPHA, logger);
		
		return idividualClone;
	}
}
