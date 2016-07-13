package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;

import jade.content.Concept;

/**
 * Ontology which represents Problem to solve
 */
public abstract class Problem implements Concept {

	private static final long serialVersionUID = 1L;
	
	public abstract String getProblemFileName();
	public abstract void setProblemFileName(String fileName);
	
	/**
	 * Reports whether it is a maximization or minimization Problem
	 * @return True for maximization Problem
	 */
	public abstract boolean isMaximizationProblem();
	
	/**
	 * Tests whether the Individual is valid solution of the Problem
	 * @param individual
	 * @param logger
	 * @return
	 */
	public abstract boolean testIsValid(Individual individual, IAgentLogger logger);
	
	
	public abstract Problem deepClone();
}