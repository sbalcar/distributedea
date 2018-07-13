package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class ToolSGDist1RandomMF {

//	static double STEP_ALPHA = 0.0002;
	static double STEP_ALPHA = 0.02;
	
	
	public static IndividualLatentFactors improve(
			IndividualLatentFactors idividual,
			ProblemMatrixFactorization problemMF,
			DatasetMF datasetMF, IAgentLogger logger) {

		RatingModel datasetModel = datasetMF.exportTrainingRatingModel();
		
		ObjectRatingList raitings = datasetModel.exportObjectRaitingList();
		ObjectRating raiting = raitings.exportRandomObjectRaiting();

		int rowIndex = datasetModel.exportIndexOfUser(raiting.getUserID());
		int colIndex = datasetModel.exportIndexOfItem(raiting.getItemID());
		double raitingValue = raiting.getRaiting();

		
		IndividualLatentFactors indivClone =
				(IndividualLatentFactors) idividual.deepClone();
		improveObjectRaiting(indivClone, rowIndex, colIndex, raitingValue,
				STEP_ALPHA, logger);
		
		return indivClone;
	}
	
	static void improveObjectRaiting(IndividualLatentFactors idividual,
			int rowIndex, int colIndex, double raitingValue, double stepAlpha,
			IAgentLogger logger) {
		
		double raitingEstim = idividual.exportValue(
				rowIndex, colIndex);

		LatentFactor latFactX = idividual.getLatentFactorX();
		LatentFactor latFactY = idividual.getLatentFactorY();
		
		LatentFactorVector vectorX =
				latFactX.exportLatentFactorVector(colIndex);
		LatentFactorVector vectorY =
				latFactY.exportLatentFactorVector(rowIndex);
		
		double e = raitingValue -raitingEstim;
		LatentFactorVector derivativesXE2 = vectorX.multiply(-2*e);
		LatentFactorVector derivativesYE2 = vectorY.multiply(-2*e);
		
		Random random = new Random();
		double shift = random.nextDouble() * stepAlpha;
		
		LatentFactorVector shiftX = derivativesYE2.multiply(shift);
		LatentFactorVector shiftY = derivativesXE2.multiply(shift);		
		
		LatentFactorVector newVectorX = vectorX.minus(shiftX);
		LatentFactorVector newVectorY = vectorY.minus(shiftY);
		
		latFactX.importLatentFactorVector(colIndex, newVectorX);
		latFactY.importLatentFactorVector(rowIndex, newVectorY);
	}
}
