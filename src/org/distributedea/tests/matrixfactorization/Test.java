package org.distributedea.tests.matrixfactorization;

import java.io.File;
import java.io.IOException;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsArithmeticSequence;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsComplement;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolGenerateIndividualMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolSGDist1RandomMF;

public class Test {

	public static void main(String [ ] args) throws IOException {
		
		fitnessOfRandomIndiv();
			
	}
	
	private static void fitnessOfRandomIndiv() {
	
		ProblemMatrixFactorization problemMF =
				new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 10);

		RatingIDsArithmeticSequence sequence = new RatingIDsArithmeticSequence(5, 5);
						
		File file =
				new File("inputs" + File.separator + "ml-100k" + File.separator + "u.data");
//		        new File("inputs" + File.separator + "ml-1m" + File.separator + "ratings.dat");
//		        new File("inputs" + File.separator + "ml-10M100K" + File.separator + "ratings.dat");
		
		DatasetDescriptionMF datasetDescr = new DatasetDescriptionMF(
				file, new RatingIDsComplement(sequence), file, sequence,
				null, null);

		
		DatasetMF datasetMF = ToolReadDatasetMF.readDatasetWithoutContent(
				datasetDescr, problemMF, new TrashLogger());
		
				
		System.out.println("Dataset readed");
		
		IndividualLatentFactors individualLF = 
				ToolGenerateIndividualMF.generateIndividual(
						problemMF, datasetMF, new TrashLogger());
		
		double fitnessTrain = ToolFitnessRMSEMF.evaluateTrainingDataset(
				individualLF, problemMF, datasetMF, new TrashLogger());
		
		System.out.println("Fi: " + fitnessTrain);
		
		
		IndividualLatentFactors individualLFI = individualLF;
		for (int i = 0; i < 20000; i++) {
			
			individualLFI = ToolSGDist1RandomMF.improve(individualLFI,
					problemMF, datasetMF, new TrashLogger());
			double fitnessI = ToolFitnessRMSEMF.evaluateTrainingDataset(
					individualLFI, problemMF, datasetMF, new TrashLogger());
		
			System.out.println("Fi" + i + ": " + fitnessI);
		}
		
		double fitnessResult = ToolFitnessRMSEMF.evaluateTestingDataset(
				individualLFI, problemMF, datasetMF, new TrashLogger());

		System.out.println("");
		System.out.println("Testing");
		System.out.println("Fi: " + fitnessResult);

	}
	
}
