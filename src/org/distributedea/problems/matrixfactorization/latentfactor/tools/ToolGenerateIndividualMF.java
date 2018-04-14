package org.distributedea.problems.matrixfactorization.latentfactor.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.ILatFactDefinition;
import org.distributedea.ontology.problem.matrixfactorization.LatFactRange;
import org.distributedea.ontology.problem.matrixfactorization.LatFactRangeSpec;

public class ToolGenerateIndividualMF {

	public static IndividualLatentFactors generateIndividual(ProblemMatrixFactorization problemMF,
			DatasetMF datasetMF, IAgentLogger logger) {
		
		ILatFactDefinition userIdsDef = problemMF.getLatFactYDef();
		ILatFactDefinition itemIdsDef = problemMF.getLatFactXDef();
		
		int width = problemMF.getLatentFactorWidth();
		
		int numberOfUsers;
		int numberOfItems;
		
		if (userIdsDef instanceof LatFactRange) {
			numberOfUsers = datasetMF.exportMaxUserID()
					-datasetMF.exportMinUserID() +1;
			
		} else if (userIdsDef instanceof LatFactRangeSpec) {
			LatFactRangeSpec latFactRangeSpec = (LatFactRangeSpec) userIdsDef;
			
			numberOfUsers = latFactRangeSpec.getMax()
					-latFactRangeSpec.getMin() +1;
		} else {
			numberOfUsers = datasetMF.exportNumberOfUsers();
		}

		
		if (itemIdsDef instanceof LatFactRange) {
			numberOfItems = datasetMF.exportMaxItemID()
					-datasetMF.exportMinItemID() +1;
			
		} else if (itemIdsDef instanceof LatFactRangeSpec) {
			LatFactRangeSpec latFactRangeSpec = (LatFactRangeSpec) itemIdsDef;
			
			numberOfItems = latFactRangeSpec.getMax()
					-latFactRangeSpec.getMin() +1;
		} else {
			numberOfItems = datasetMF.exportNumberOfItems();
		}
		
		LatentFactor latentFactorX =
				generateLatentFactor(numberOfItems, width);
		LatentFactor latentFactorY =
				generateLatentFactor(numberOfUsers, width);
				
		return new IndividualLatentFactors(latentFactorX, latentFactorY);
	}
	
	private static LatentFactor generateLatentFactor(int length, int width) {

		List<LatentFactorVector> vectorList = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			
			LatentFactorVector vectorI =
					generateVector(width);
			
			vectorList.add(vectorI);
		}

		return new LatentFactor(vectorList);
	}
	
	private static LatentFactorVector generateVector(int width) {
		
		double[] vector = new double[width];
		
		for (int i = 0; i < width; i++) {
			vector[i] = Math.random();
		}
		
		return new LatentFactorVector(vector);
	}
}
