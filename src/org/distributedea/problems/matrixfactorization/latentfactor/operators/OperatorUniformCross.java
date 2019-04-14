package org.distributedea.problems.matrixfactorization.latentfactor.operators;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorModel;

public class OperatorUniformCross {

	public static IndividualLatentFactors[] create(
			IndividualLatentFactors individualLF1,
			IndividualLatentFactors individualLF2) {
		
		LatentFactorModel model1 = individualLF1.getLatentFactorModel();
		LatentFactorModel model2 = individualLF2.getLatentFactorModel();

		LatentFactor latFat1X = model1.getLatentFactorX();
		LatentFactor latFat1Y = model1.getLatentFactorY();
		
		LatentFactor latFat2X = model2.getLatentFactorX();
		LatentFactor latFat2Y = model2.getLatentFactorY();
		
		LatentFactor latFatNewX = uniformCross(latFat1X, latFat2X);
		LatentFactor latFatNewY = uniformCross(latFat1Y, latFat2Y);
		
		LatentFactorModel modelNew =
				new LatentFactorModel(latFatNewX, latFatNewY);
		IndividualLatentFactors indivNew =
				new IndividualLatentFactors(modelNew);
		
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
