package org.distributedea.ontology.dataset;

import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

/**
 * Ontology which represents assignment of data set
 * @author stepan
 */
public abstract class Dataset implements Concept {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Tests validity
	 * @return
	 */
	public abstract boolean valid(IAgentLogger logger);
	
	/**
	 * Returns clone
	 * @return
	 */
	public abstract Dataset deepClone();
	
	/**
	 * Test if this {@link Problem} is instance of given class
	 * @param problemClass
	 * @return
	 */
	public boolean theSameClass(Class<?> problemClass) {
		if (getClass() == problemClass) {
			return true;
		}
		return false;
	}
	 
}