package org.distributedea.tests.matrixfactorization.mahout;

import java.io.File;

import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ParallelSGDFactorizerWrp;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ReadDatasetExample;


public class MahoutApacheTest {

	public static void main(String [ ] args) throws Exception {
		
		System.out.println("Test content");

		File f = new File(FileNames.getDirectoryOfInputs() + File.separator + "datasetMFExample.csv");

		RatingModel rModel = ReadDatasetExample.read(f);

		//MF recommender model
	   	//DataModel model = new FileDataModel(f);
		DataModel model = ReadDatasetExample.convertToDataModel(rModel);
		
	   	//ALSWRFactorizer factorizer = new ALSWRFactorizer(model, 50, 0.065, 15);
	   	//ParallelSGDFactorizer factorizer = new ParallelSGDFactorizer(model,10,0.1,1);
		ParallelSGDFactorizerWrp factorizer = new ParallelSGDFactorizerWrp(model,12,0.1,10000);
	   	@SuppressWarnings("unused")
		SVDRecommender recommender = new SVDRecommender(model, factorizer);    	
	   	

	   	// counts rmse
	   	double squares = 0;
	   	for (ObjectRating oRatingI : rModel.exportRatingsClone()) {
	   		int userIndex = rModel.exportIndexOfUser(oRatingI.getUserID());
	   		int itemIndex = rModel.exportIndexOfItem(oRatingI.getItemID());
	   		
	   		double predictedRatingI = factorizer.rating(
	   				userIndex, itemIndex);
	   		squares += Math.pow(oRatingI.getRaiting() - predictedRatingI, 2);
	   	}
	   	double square = squares / rModel.exportNumberOfRaitings();
	   	double rmse = Math.sqrt(square);
	   	
	   	
//	   	List<RecommendedItem> recommendations = recommender.recommend(2, 3);
//	   	for (RecommendedItem recommendation : recommendations) {
//	   	  System.out.println(recommendation);
//	   	}
	   	
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
