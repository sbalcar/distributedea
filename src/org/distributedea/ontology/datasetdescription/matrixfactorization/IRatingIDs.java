package org.distributedea.ontology.datasetdescription.matrixfactorization;

import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

public interface IRatingIDs extends Concept {

	/**
	 * Contains rating ID
	 * @param ratingID
	 * @return
	 */
	public boolean containsRatingID(int ratingID);
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger);
	
	/**
	 * Returns clone
	 * @return
	 */
	public IRatingIDs deepClone();
	
	/**
	 * Returns equals
	 * @return
	 */	
	public boolean equals(Object other);
}
