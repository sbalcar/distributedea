package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.DatasetModel;
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
	
		DatasetModel datasetModel = datasetMF.exportTrainingDatasetModel();
		
		int index = (int) (neighborIndex % datasetModel.exportNumberOfRaitings());
		
		ObjectRaitingList raitings = datasetModel.exportObjectRaitingList();
		ObjectRaiting raiting = raitings.getRaitings().get(index);

		int rowIndex = datasetModel.exportIndexOfUser(raiting.getUserID());
		int colIndex = datasetModel.exportIndexOfItem(raiting.getItemID());
		double raitingValue = raiting.getRaiting();

		IndividualLatentFactors idividualClone =
				(IndividualLatentFactors) idividualLF.deepClone();
		
		ToolSGDist1RandomMF.improveObjectRaiting(idividualClone,
				rowIndex, colIndex, raitingValue, STEP_ALPHA, logger);
		
		return idividualClone;
	}
}
