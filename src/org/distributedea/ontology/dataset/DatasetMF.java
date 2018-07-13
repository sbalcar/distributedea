package org.distributedea.ontology.dataset;

import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.matrixfactorization.IItemContentModel;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.content.ContentMLMoviesModel;
import org.distributedea.ontology.dataset.matrixfactorization.content.IItemContent;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;

/**
 * Ontology represents matrix factorization {@link Dataset}
 * @author stepan
 *
 */
public class DatasetMF extends Dataset {

	private static final long serialVersionUID = 1L;

	/** RatingModel optimized Model - no ontology **/
	private RatingModel trainingRatingsModel;
	
	private RatingModel testingRatingsModel;
	
	/** ItemContentModel optimized Model - no ontology **/
	private IItemContentModel itemContentModel;
	
	
	@Deprecated
	public DatasetMF() { // Only for Jade
	}

	/**
	 * Constructor
	 * @param problemFile
	 */
	public DatasetMF(List<ObjectRating> trainingRatings, List<ObjectRating> testingRatings,
			List<IItemContent> itemsContent) {
		
		setTrainingRatings(trainingRatings);
		setTestingRatings(testingRatings);

		if (itemsContent != null) {
			setItemContentsModel(itemsContent);
		}
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

		this.trainingRatingsModel = dataset.trainingRatingsModel;
		this.testingRatingsModel = dataset.testingRatingsModel;
		
		this.itemContentModel = dataset.itemContentModel;
	}
	

	public List<ObjectRating> getTrainingRatings() {
		return trainingRatingsModel.exportRatingsClone();
	}

	@Deprecated
	public void setTrainingRatings(List<ObjectRating> ratings) {
		if (ratings == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRating raitingI : ratings) {
			if (raitingI == null || (! raitingI.valid(new TrashLogger()))) {
				throw new IllegalArgumentException("Argument " +
						ObjectRating.class.getSimpleName() + " is not valid");
			}
		}
		
		this.trainingRatingsModel = new RatingModel(ratings);
	}
	
	
	public List<ObjectRating> getTestingRatings() {
		return testingRatingsModel.exportRatingsClone();
	}
	
	@Deprecated
	public void setTestingRatings(List<ObjectRating> ratings) {
		if (ratings == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		for (ObjectRating ratingI : ratings) {
			if (ratingI == null || (! ratingI.valid(new TrashLogger()))) {
				throw new IllegalArgumentException("Argument " +
						ObjectRating.class.getSimpleName() + " is not valid");
			}
		}
		
		this.testingRatingsModel = new RatingModel(ratings);
	}
	

	public List<IItemContent> getItemContentsModel() {
		return itemContentModel.exportIItemContents();
	}
	@Deprecated
	public void setItemContentsModel(List<IItemContent> itemsContent) {
		
		// if dataset == ML
		this.itemContentModel = new ContentMLMoviesModel(itemsContent);
	}

	
	public RatingModel exportTrainingRatingModel() {
		return this.trainingRatingsModel;
	}

	public RatingModel exportTestingRatingModel() {
		return this.testingRatingsModel;
	}
	
	public IItemContentModel exportItemContentModel() {
		return this.itemContentModel;
	}
	
	
	@Override
	public boolean valid(IAgentLogger logger) {

		if (this.trainingRatingsModel == null) {
			return false;
		}

		if (this.testingRatingsModel == null) {
			return false;
		}
		
		return true;
	}

	@Override
	public DatasetMF deepClone() {
		
		return new DatasetMF(this);
	}

}


