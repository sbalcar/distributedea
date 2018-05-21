package org.distributedea.ontology.problem.matrixfactorization.traintest;

import org.distributedea.logging.IAgentLogger;

/**
 * Ontology represents empty set of RatingID
 * @author stepan
 *
 */
public class RatingIDsEmptySet implements IRatingIDs {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean containsRatingID(int ratingID) {
		return false;
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public IRatingIDs deepClone() {
		return new RatingIDsEmptySet();
	}

	@Override
	public boolean equals(Object other) {
		return true;
	}
	
}
