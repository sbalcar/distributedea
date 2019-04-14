package org.distributedea.ontology.problem;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;

public interface IProblem extends Concept {

	/**
	 * Exports type (maximization/minimization) problem
	 * @return
	 */
	public boolean exportIsMaximizationProblem();
	
	/**
	 * Export Dataset representation class
	 * @return
	 */
	public Class<?> exportDatasetClass();
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger);
	
	/**
	 * Returns clone
	 * @return
	 */
	public AProblem deepClone();
	
	/**
	 * Log String
	 * @return
	 */
	public String toLogString();
	
	public boolean equals(Object other);
}
