package org.distributedea.problemtools.matrixfactorization.latentfactor.operators;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

public class OperatorDifferential {

	public static IndividualLatentFactors create(IndividualLatentFactors individualLF1,
			IndividualLatentFactors individualLF2, IndividualLatentFactors individualLF3,
			double differentialWeightF, ProblemMatrixFactorization problemMF,
			DatasetMF datasetMF, IAgentLogger logger) throws Exception {
		
		LatentFactor latFat1X = individualLF1.getLatentFactorX();
		LatentFactor latFat1Y = individualLF1.getLatentFactorY();
		
		LatentFactor latFat2X = individualLF2.getLatentFactorX();
		LatentFactor latFat2Y = individualLF2.getLatentFactorY();

		LatentFactor latFat3X = individualLF3.getLatentFactorX();
		LatentFactor latFat3Y = individualLF3.getLatentFactorY();

		
		LatentFactor latFatX = differential(latFat1X, latFat2X, latFat3X, differentialWeightF);
		LatentFactor latFatY = differential(latFat1Y, latFat2Y, latFat3Y, differentialWeightF);
		
		return new IndividualLatentFactors(latFatX, latFatY);
	}
	
	private static LatentFactor differential(LatentFactor latFat1, LatentFactor latFat2,
			LatentFactor latFat3, double differentialWeightF) {
		
		List<LatentFactorVector> vectorList = new ArrayList<>();
		
		for (int i = 0; i < latFat1.size(); i++) {
			
			LatentFactorVector vector1 = latFat1.exportLatentFactorVector(i);
			LatentFactorVector vector2 = latFat2.exportLatentFactorVector(i);
			LatentFactorVector vector3 = latFat3.exportLatentFactorVector(i);
			
			LatentFactorVector diff = vector2.minus(vector3);
			
			LatentFactorVector vectorNew = vector1.plus(
					diff.multiply(differentialWeightF));
			
			vectorList.add(vectorNew);
		}
		
		return new LatentFactor(vectorList);
	}
	
}
