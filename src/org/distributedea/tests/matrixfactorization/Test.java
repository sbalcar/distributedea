package org.distributedea.tests.matrixfactorization;

import java.io.File;
import java.io.IOException;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.DatasetPartitioning;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.ontology.problem.matrixfactorization.traintest.RatingIDsArithmeticSequence;
import org.distributedea.ontology.problem.matrixfactorization.traintest.RatingIDsComplement;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolGenerateIndividualMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolSGDist1RandomMF;

public class Test {

	public static void main(String [ ] args) throws IOException {
		
		fitnessOfRandomIndiv();
			
	}
	
	private static void fitnessOfRandomIndiv() {
	
		DatasetPartitioning datasetPartitioning = new DatasetPartitioning(
				new RatingIDsComplement(new RatingIDsArithmeticSequence(5, 5)),
				new RatingIDsArithmeticSequence(5, 5));
		
		ProblemMatrixFactorization problemMF =
				new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10, datasetPartitioning);
		
		DatasetMF datasetTrainMF = ToolReadDatasetMF.readTrainingPartOfDataset(
				new File("inputs" + File.separator + "ml-100k" + File.separator + "u.data"),
//				new File("inputs" + File.separator + "ml-1m" + File.separator + "ratings.dat"),
//				new File("inputs" + File.separator + "ml-10M100K" + File.separator + "ratings.dat"),
				problemMF, new TrashLogger());
		System.out.println("Dataset readed");
		
		IndividualLatentFactors individualLF = 
				ToolGenerateIndividualMF.generateIndividual(
						problemMF, datasetTrainMF, new TrashLogger());
		
		double fitness = ToolFitnessRMSEMF.evaluate(individualLF, problemMF,
				datasetTrainMF, new TrashLogger());
		
		System.out.println("Fi: " + fitness);
		
		
		IndividualLatentFactors individualLFI = individualLF;
		for (int i = 0; i < 20000; i++) {
			
			individualLFI = ToolSGDist1RandomMF.improve(individualLFI,
					problemMF, datasetTrainMF, new TrashLogger());
			double fitnessI = ToolFitnessRMSEMF.evaluate(individualLFI,
					problemMF, datasetTrainMF, new TrashLogger());
		
			System.out.println("Fi" + i + ": " + fitnessI);
		}
		
		DatasetMF datasetTestMF = ToolReadDatasetMF.readTestingPartOfDataset(
				new File("inputs" + File.separator + "ml-100k" + File.separator + "u.data"),
				problemMF, new TrashLogger());

		double fitnessResult = ToolFitnessRMSEMF.evaluate(individualLFI, problemMF,
				datasetTestMF, new TrashLogger());

		System.out.println("");
		System.out.println("Testing");
		System.out.println("Fi: " + fitnessResult);

	}
	
}
