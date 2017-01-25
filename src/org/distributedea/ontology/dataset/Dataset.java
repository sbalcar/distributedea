package org.distributedea.ontology.dataset;

import java.io.File;

import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

/**
 * Ontology which represents assignment of data set
 * @author stepan
 */
public abstract class Dataset implements Concept {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Export {@link File} with {@link Dataset}
	 * @return
	 */
	public abstract File exportProblemFile();
	/**
	 * Import {@link File} with {@link Dataset} to solve
	 * @param problemFile
	 */
	public abstract void importProblemFile(File problemFile);
	

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