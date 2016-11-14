package org.distributedea.ontology.individuals;

import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

/**
 * Abstract Ontology represents one solution
 * @author stepan
 *
 */
public abstract class Individual implements Concept {

	private static final long serialVersionUID = 1L;


	/**
	 * Tests validity of Individual. Method don't has name "isvalid()" because
	 * prefix is has for Jade Ontology special significance.
	 * @return
	 */
	public abstract boolean valid(IAgentLogger logger);
	
	/**
	 * Test if this {@link Individual} is instance of given class
	 * @param individualClass
	 * @return
	 */
	public boolean theSameClass(Class<?> individualClass) {
		if (getClass() == individualClass) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public abstract Individual deepClone();
	
	/**
	 * Returns Individual as user friendly String.
	 * @return
	 */
	public abstract String toLogString();
	
}
