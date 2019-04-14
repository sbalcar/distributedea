package org.distributedea.tests.matrixfactorization.mahout;

import java.io.File;

import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsArithmeticSequence;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsComplement;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ParallelSGDFactorizerWrp;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ReadDatasetExample;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.readingdataset.ToolReadDatasetMF;

public class MahoutBasedIndividualMLTest {

	public static void main(String [ ] args) throws Exception {
		
		System.out.println("Test content");

		ProblemMatrixFactorization problemMF = new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 15);
		
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

/*
		DatasetDescriptionMF datasetDescr = new DatasetDescriptionMF(
				fileRatings, new RatingIDsFullSet(),
				fileRatings, new RatingIDsEmptySet(),
				fileItems, fileUsers);
*/
		
		DatasetMF datasetMF = ToolReadDatasetMF.readDatasetWithoutContent(
				datasetDescr, problemMF, new TrashLogger());
		
		RatingModel rModel = datasetMF.exportTrainingRatingModel();
		
		//MF recommender mahout model
		DataModel model = ReadDatasetExample.convertToDataModel(rModel);
		
		ParallelSGDFactorizerWrp factorizer = new ParallelSGDFactorizerWrp(
				model, problemMF.getLatentFactorWidth() -3, 0.1, 1000);
	   	@SuppressWarnings("unused")
	   	SVDRecommender recommender = new SVDRecommender(model, factorizer);

	   	
	   	IndividualLatentFactors individualLF = new IndividualLatentFactors(factorizer);
	   	//double rmse = ToolFitnessRMSEMF.evaluateTestingDataset(individualLF, problemMF, datasetMF, new TrashLogger());
	   	double rmse = ToolFitnessRMSEMF.evaluateTrainingDataset(individualLF, problemMF, datasetMF, new TrashLogger());

	   	
	   	System.out.println("Min user ID: " + rModel.exportMinUserID());
	   	System.out.println("Max user ID: " + rModel.exportMaxUserID());

	   	System.out.println("Min item ID: " + rModel.exportMinItemID());
	   	System.out.println("Max item ID: " + rModel.exportMaxItemID());

	   	System.out.println("Num users: " + rModel.exportNumberOfUsers());
	   	System.out.println("Num items: " + rModel.exportNumberOfItems());

	   	
	   	System.out.println("Length of users vector: " + factorizer.numberOfUsers());
	   	System.out.println("Length of items vector: " + factorizer.numberOfItems());
	   	System.out.println("Width Of LatentVector: " + factorizer.widthOfLatentVector());
	   	
	   	System.out.println("Rating: " + factorizer.rating(0, 0));
	   	System.out.println("Rating: " + factorizer.rating(0, 1));
	   	System.out.println("Rating: " + factorizer.rating(0, 2));
	   	System.out.println("Rating: " + factorizer.rating(0, 3));
	   	System.out.println("Rating: " + factorizer.rating(0, 4));
	   	//3699
	   	System.out.println("RMSE: " + rmse);
	}

}
