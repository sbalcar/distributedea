package org.distributedea.tests.matrixfactorization.mahout;

import java.io.File;

import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ParallelSGDFactorizerWrp;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ReadDatasetExample;

public class MahoutBasedIndividualTest {

	public static void main(String [ ] args) throws Exception {
		
		System.out.println("Test content");

		ProblemMatrixFactorization problemMF = new ProblemMatrixFactorization(
				new LatFactRange(), new LatFactRange(), 15);
		
		File f = new File(FileNames.getDirectoryOfInputs() + File.separator + "datasetMFExample.csv");

		RatingModel rModel = ReadDatasetExample.read(f);
		
		DatasetMF datasetMF = new DatasetMF(
				rModel.exportRatingsClone(), rModel.exportRatingsClone(), null);

		//MF recommender model
		DataModel model = ReadDatasetExample.convertToDataModel(rModel);
		
		ParallelSGDFactorizerWrp factorizer = new ParallelSGDFactorizerWrp(
				model, problemMF.getLatentFactorWidth() -3, 0.1, 10000);
	   	@SuppressWarnings("unused")
	   	SVDRecommender recommender = new SVDRecommender(model, factorizer);

	   	
	   	IndividualLatentFactors individualLF = new IndividualLatentFactors(factorizer);
	   	double rmse = ToolFitnessRMSEMF.evaluateTestingDataset(individualLF, problemMF, datasetMF, new TrashLogger());
	   		   	
	   	System.out.println("Number of users: " + factorizer.numberOfUsers());
	   	System.out.println("Number of items: " + factorizer.numberOfItems());
	   	System.out.println("Width Of LatentVector: " + factorizer.widthOfLatentVector());
	   	
	   	System.out.println("Rating: " + factorizer.rating(0, 0));
	   	System.out.println("Rating: " + factorizer.rating(0, 1));
	   	System.out.println("Rating: " + factorizer.rating(0, 2));
	   	System.out.println("Rating: " + factorizer.rating(0, 3));
	   	System.out.println("Rating: " + factorizer.rating(0, 4));
	   	
	   	System.out.println("RMSE: " + rmse);
	}
}
