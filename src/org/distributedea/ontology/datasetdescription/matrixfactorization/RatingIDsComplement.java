package org.distributedea.ontology.datasetdescription.matrixfactorization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology represents complement of {@link IRatingIDs}
 * @author stepan
 *
 */
public class RatingIDsComplement implements IRatingIDs {

	private static final long serialVersionUID = 1L;

	private IRatingIDs ratingIDs;
	
	@Deprecated
	public RatingIDsComplement() {  // Only for Jade
		this.setRatingIDs(new RatingIDsFullSet());
	}

	/**
	 * Constructor
	 * @param ratingIDs
	 */
	public RatingIDsComplement(IRatingIDs ratingIDs) {
		this.setRatingIDs(ratingIDs);
	}

	/**
	 * Copy constructor
	 * @param ratingIDs
	 */
	public RatingIDsComplement(RatingIDsComplement complement) {
		if (complement == null || ! complement.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					RatingIDsComplement.class.getSimpleName() + " is not valid");
		}
		this.setRatingIDs(complement.getRatingIDs().deepClone());
	}
		
	public IRatingIDs getRatingIDs() {
		return ratingIDs;
	}
	public void setRatingIDs(IRatingIDs ratingIDs) {
		if (ratingIDs == null || ! ratingIDs.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IRatingIDs.class.getSimpleName() + " is not valid");
		}
		this.ratingIDs = ratingIDs;
	}

	@Override
	public boolean containsRatingID(int ratingID) {
		return ! getRatingIDs().containsRatingID(ratingID);
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		return getRatingIDs() != null && getRatingIDs().valid(logger);
	}

	@Override
	public IRatingIDs deepClone() {
		return new RatingIDsComplement(this);
	}

	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof RatingIDsComplement)) {
	        return false;
	    }
	    
	    RatingIDsComplement otherComplement = (RatingIDsComplement)other;
	    
	    boolean isEqual = false;
	    
	    if (getRatingIDs() == null && otherComplement.getRatingIDs() == null) {
	    	isEqual = true;
	    }
	    if (getRatingIDs() != null &&
	    		getRatingIDs().equals(otherComplement.getRatingIDs())) {
	    	isEqual = true;
	    }
	    
	    return isEqual;
	}

}
