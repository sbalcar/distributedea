package org.distributedea.tests.matrixfactorization.mahout;

import java.io.File;

import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsArithmeticSequence;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsComplement;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolHillClimbingMFMahout;
import org.distributedea.problems.matrixfactorization.latentfactor.operators.OperatorReplaceDiffOfOneRandomLatentFactor;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.ToolGenerateMaboutModelMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.readingdataset.ToolReadDatasetMF;

public class MahoutProblemToolTest {

	public static void main(String [ ] args) throws Exception {
		
		ProblemMatrixFactorization problemMF = new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 100);
		
		File fileRatings = new File(FileNames.getInputProblemFile(
				"ml-100k" + File.separator + "u.data"));
		File fileItems = new File(FileNames.getInputProblemFile(
				"ml-100k" + File.separator + "u.item"));
		File fileUsers = new File(FileNames.getInputProblemFile(
				"ml-100k" + File.separator + "u.user"));
		
		
		RatingIDsArithmeticSequence sequence =
				new RatingIDsArithmeticSequence(20, 20);
		DatasetDescriptionMF datasetDescr = new DatasetDescriptionMF(
				fileRatings, sequence,
				fileRatings, new RatingIDsComplement(sequence),
				fileItems, fileUsers);

		DatasetMF datasetMF = ToolReadDatasetMF.readDatasetWithoutContent(
				datasetDescr, problemMF, new TrashLogger());
		
		IndividualLatentFactors individualLF = ToolGenerateMaboutModelMF.generateMaboutModel(
				problemMF, datasetMF, new TrashLogger());
		double rmse = ToolFitnessRMSEMF.evaluateTrainingDataset(
				individualLF, problemMF, datasetMF, new TrashLogger());
		IndividualEvaluated individualEval = new IndividualEvaluated(individualLF, rmse, null);
		System.out.println("rmse : " + rmse);
		
		
		ProblemToolHillClimbingMFMahout tool = new ProblemToolHillClimbingMFMahout();
		
		for (int i = 0; i < 100; i++) {
			IndividualLatentFactors individualI =
					OperatorReplaceDiffOfOneRandomLatentFactor.improve(
					individualLF, problemMF, datasetMF, new TrashLogger());
			double rmseI = ToolFitnessRMSEMF.evaluateTrainingDataset(
					individualI, problemMF, datasetMF, new TrashLogger());
	
			System.out.println("rmseI: " + rmseI);
			if (rmseI < rmse) {
				System.out.println("JE LEPSI !!!");
			}
		}
	}
}
