package org.distributedea.ontology.problemdefinition;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;

public interface IProblemDefinition extends Concept {

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
	public AProblemDefinition deepClone();
	
	
	public boolean equals(Object other);
}
