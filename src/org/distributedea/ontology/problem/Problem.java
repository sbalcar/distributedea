package org.distributedea.ontology.problem;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;

import jade.content.Concept;

/**
 * Ontology which represents assignment of problem to solve
 * @author stepan
 */
public abstract class Problem implements Concept {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Export {@link File} with {@link Problem}
	 * @return
	 */
	public abstract File exportProblemFile();
	/**
	 * Import {@link File} with {@link Problem} to solve
	 * @param problemFile
	 */
	public abstract void importProblemFile(File problemFile);
	
	/**
	 * Reports whether it is a maximization or minimization Problem
	 * @return True for maximization Problem
	 */
//	public abstract boolean isMaximizationProblem();
	
	/**
	 * Tests whether the Individual is valid solution of the Problem
	 * @param individual
	 * @param logger
	 * @return
	 */
	public abstract boolean testIsIGivenIndividualSolutionOfTheProblem(
			Individual individual, IAgentLogger logger);

	/**
	 * Tests validity
	 * @return
	 */
	public abstract boolean valid(IAgentLogger logger);
	
	/**
	 * Returns clone
	 * @return
	 */
	public abstract Problem deepClone();
	
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
	
	/**
	 * Creates {@link Problem} instance from Class
	 * @param problemClass
	 * @return
	 */
	public static Problem createProblem(Class<?> problemClass) {
		
		Problem problem = null;
		try {
			problem = (Problem)problemClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
		
		return problem;
	} 
}