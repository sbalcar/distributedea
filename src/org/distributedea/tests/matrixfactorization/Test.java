package org.distributedea.tests.matrixfactorization;

import java.io.File;
import java.io.IOException;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.LatFactRange;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolGenerateIndividualMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolSGDist1RandomMF;

public class Test {

	public static void main(String [ ] args) throws IOException {
		
		fitnessOfRandomIndiv();
			
	}
	
	private static void fitnessOfRandomIndiv() {
		
		ProblemMatrixFactorization problemMF =
				new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10);
		
		DatasetMF datasetMF = ToolReadDatasetMF.readDataset(
				new File("inputs/ml-100k/u.data"), new TrashLogger());
//				new File("inputs/ml-1m/ratings.dat"), new TrashLogger());
//				new File("inputs/ml-10M100K/ratings.dat"), new TrashLogger());
		System.out.println("Dataset readed");
		
		IndividualLatentFactors individualLF = 
				ToolGenerateIndividualMF.generateIndividual(
						problemMF, datasetMF, new TrashLogger());
		
		double fitness = ToolFitnessRMSEMF.evaluate(individualLF, problemMF,
				datasetMF, new TrashLogger());
		
		System.out.println("Fi: " + fitness);
		
		
		IndividualLatentFactors individualLFI = individualLF;
		for (int i = 0; i < 100000; i++) {
			
			individualLFI = ToolSGDist1RandomMF.improve(individualLFI, problemMF,
							datasetMF, new TrashLogger());
			double fitnessI = ToolFitnessRMSEMF.evaluate(individualLFI,
					problemMF, datasetMF, new TrashLogger());
		
			System.out.println("Fi" + i + ": " + fitnessI);
		}
	}
	
}
