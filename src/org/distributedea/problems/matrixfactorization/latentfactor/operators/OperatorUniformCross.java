package org.distributedea.problems.matrixfactorization.latentfactor.operators;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;

public class OperatorUniformCross {

	public static IndividualLatentFactors[] create(
			IndividualLatentFactors individualLF1,
			IndividualLatentFactors individualLF2) {
		
		LatentFactor latFat1X = individualLF1.getLatentFactorX();
		LatentFactor latFat1Y = individualLF1.getLatentFactorY();
		
		LatentFactor latFat2X = individualLF2.getLatentFactorX();
		LatentFactor latFat2Y = individualLF2.getLatentFactorY();
		
		LatentFactor latFatNewX = uniformCross(latFat1X, latFat2X);
		LatentFactor latFatNewY = uniformCross(latFat1Y, latFat2Y);
		
		IndividualLatentFactors indivNew =
				new IndividualLatentFactors(latFatNewX, latFatNewY);
		
		IndividualLatentFactors[] result = new IndividualLatentFactors[2];
		result[0] = indivNew;
		result[1] = indivNew;
		
		return result;
	}
	
	private static LatentFactor uniformCross(LatentFactor latFat1,
			LatentFactor latFat2) {
		
		List<LatentFactorVector> newVectorsList = new ArrayList<>();
		
		for (int i = 0; i < latFat1.size(); i++) {
			
			LatentFactorVector vectorNew = null;
			
			if (Math.random() < 0.5) {
				vectorNew = latFat1.exportLatentFactorVector(i);
			} else {
				vectorNew = latFat2.exportLatentFactorVector(i);
			}
			
			newVectorsList.add(vectorNew.deepClone());
		}
		
		return new LatentFactor(newVectorsList);
	}
	
}
