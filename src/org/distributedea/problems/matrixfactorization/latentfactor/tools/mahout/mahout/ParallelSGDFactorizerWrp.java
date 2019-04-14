package org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.svd.ParallelSGDFactorizer;
import org.apache.mahout.cf.taste.model.DataModel;

public class ParallelSGDFactorizerWrp extends ParallelSGDFactorizer {

	public ParallelSGDFactorizerWrp(DataModel dataModel, int numFeatures,
			double lambda, int numEpochs) throws TasteException {
		super(dataModel, numFeatures, lambda, numEpochs);
	}
	
	public int numberOfUsers() {
		return this.userVectors.length;
	}

	public int numberOfItems() {
		return this.itemVectors.length;
	}

	public int widthOfLatentVector() {
		return this.userVectors[0].length;
	}

	public double[] getUserVector(int userIndex) {
		return this.userVectors[userIndex];
	}
	
	public double[] getItemVector(int itemIndex) {
		return this.itemVectors[itemIndex];
	}
	
	public double rating(int userIndex, int itemIndex) {
		double[] userIVector = this.userVectors[userIndex];
		double[] itemIVector = this.itemVectors[itemIndex];
		
		double result = 0;
		for (int i = 0; i < userIVector.length; i++) {
			result += userIVector[i] * itemIVector[i];
		}
		return result;
	}


}
