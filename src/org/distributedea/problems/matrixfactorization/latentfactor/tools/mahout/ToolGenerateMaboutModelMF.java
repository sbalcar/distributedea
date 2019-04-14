package org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout;

import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ParallelSGDFactorizerWrp;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout.ReadDatasetExample;

public class ToolGenerateMaboutModelMF {
	
	public static IndividualLatentFactors generateMaboutModel(
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) throws Exception {
				
		RatingModel rModel = datasetMF.exportTrainingRatingModel();
		
		//MF recommender mahout model
		DataModel model = ReadDatasetExample.convertToDataModel(rModel);
		
		ParallelSGDFactorizerWrp factorizer = new ParallelSGDFactorizerWrp(
				model, problemMF.getLatentFactorWidth() -3, 0.1, 1000);
	   	@SuppressWarnings("unused")
	   	SVDRecommender recommender = new SVDRecommender(model, factorizer);

		return new IndividualLatentFactors(factorizer);
	}
}
