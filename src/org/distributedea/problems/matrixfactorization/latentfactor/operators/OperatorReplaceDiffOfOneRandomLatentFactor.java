package org.distributedea.problems.matrixfactorization.latentfactor.operators;

import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorModel;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.ToolGenerateMaboutModelMF;

public class OperatorReplaceDiffOfOneRandomLatentFactor {

	public static IndividualLatentFactors improve(IndividualLatentFactors individualLF,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) throws Exception {
				
		// generate a new individual
		IndividualLatentFactors newIndiv = ToolGenerateMaboutModelMF
				.generateMaboutModel(problemMF, datasetMF, logger);
		newIndiv.convert();
		
		Random rand = new Random();
		int indexRand = rand.nextInt(newIndiv.getLatentFactorModel().getLatentFactorX().size());
		
		// exports one random latent factor vector
		LatentFactorModel newModel = newIndiv.getLatentFactorModel();
		LatentFactor newFactorX = newModel.getLatentFactorX();
		
		LatentFactorVector newFactorVect = newFactorX.exportLatentFactorVector(indexRand);
		
		// clone input individual
		IndividualLatentFactors individualLFClone =
				(IndividualLatentFactors) individualLF.deepClone();
		individualLFClone.convert();
		
		LatentFactorModel modelOfClone = individualLFClone.getLatentFactorModel();
		
		LatentFactor factorXOfClone = modelOfClone.getLatentFactorX();
		LatentFactorVector latFactorVectClone = factorXOfClone.exportLatentFactorVector(indexRand);
		
		LatentFactorVector diff = newFactorVect.minus(latFactorVectClone);
		LatentFactorVector bit = diff.multiply(0.001);
		
		latFactorVectClone.plus(bit);
		
		return individualLFClone;
	}

}
