package org.distributedea.ontology.problem.matrixfactorization.traintest;

import org.distributedea.logging.IAgentLogger;

/**
 * Ontology represents full set of RatingID
 * @author stepan
 *
 */
public class RatingIDsFullSet implements IRatingIDs {

	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean containsRatingID(int ratingID) {
		return true;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public IRatingIDs deepClone() {
		return new RatingIDsFullSet();
	}
	
	@Override
	public boolean equals(Object other) {
		return true;
	}

}
