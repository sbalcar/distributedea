package org.distributedea.ontology.dataset;

import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.matrixfactorization.DatasetModel;
import org.distributedea.ontology.dataset.matrixfactorization.ObjectRaiting;

/**
 * Ontology represents matrix factorization {@link Dataset}
 * @author stepan
 *
 */
public class DatasetMF extends Dataset {

	private static final long serialVersionUID = 1L;

	/** Dataset optimized Model - no ontology **/
	private DatasetModel trainingDatasetModel;
	
	private DatasetModel testingDatasetModel;
	
	@Deprecated
	public DatasetMF() { // Only for Jade
	}

	/**
	 * Constructor
	 * @param problemFile
	 */
	public DatasetMF(List<ObjectRaiting> trainingRatings, List<ObjectRaiting> testingRatings) {
		setTrainingRatings(trainingRatings);
		setTestingRatings(testingRatings);

	}

	/**
	 * Copy Constructor
	 * @param dataset
	 */
	public DatasetMF(DatasetMF dataset) {
		if (dataset == null) {
			throw new IllegalArgumentException("Argument " +
					DatasetMF.class.getSimpleName() + " is not valid");
		}

		this.trainingDatasetModel = dataset.trainingDatasetModel;
		this.testingDatasetModel = dataset.testingDatasetModel;
	}
	

	public List<ObjectRaiting> getTrainingRatings() {
		return trainingDatasetModel.exportRatingsClone();
	}

	@Deprecated
	public void setTrainingRatings(List<ObjectRaiting> ratings) {
		if (ratings == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRaiting raitingI : ratings) {
			if (raitingI == null || (! raitingI.valid(new TrashLogger()))) {
				throw new IllegalArgumentException("Argument " +
						ObjectRaiting.class.getSimpleName() + " is not valid");
			}
		}
		
		this.trainingDatasetModel = new DatasetModel(ratings);
	}
	
	
	public List<ObjectRaiting> getTestingRatings() {
		return testingDatasetModel.exportRatingsClone();
	}
	
	@Deprecated
	public void setTestingRatings(List<ObjectRaiting> ratings) {
		if (ratings == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRaiting ratingI : ratings) {
			if (ratingI == null || (! ratingI.valid(new TrashLogger()))) {
				throw new IllegalArgumentException("Argument " +
						ObjectRaiting.class.getSimpleName() + " is not valid");
			}
		}
		
		this.testingDatasetModel = new DatasetModel(ratings);
	}
		
	
	

	public DatasetModel exportTrainingDatasetModel() {
		return this.trainingDatasetModel;
	}

	public DatasetModel exportTestingDatasetModel() {
		return this.testingDatasetModel;
	}
	
	
	@Override
	public boolean valid(IAgentLogger logger) {

		if (this.trainingDatasetModel == null) {
			return false;
		}

		if (this.testingDatasetModel == null) {
			return false;
		}
		
		return true;
	}

	@Override
	public DatasetMF deepClone() {
		
		return new DatasetMF(this);
	}

}


