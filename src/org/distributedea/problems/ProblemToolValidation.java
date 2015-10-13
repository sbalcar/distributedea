package org.distributedea.problems;

import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;

/**
 * ProblemTool helper for input parameters validation
 * @author stepan
 *
 */
public class ProblemToolValidation {

	/**
	 * Verifies if individual is instance of individualClass
	 * @param individual
	 * @param individualClass
	 * @param logger
	 * @return
	 */
	public static boolean isIndividualTypeOf(Individual individual,
			Class<?> individualClass, AgentLogger logger) {
		
		if (individual.getClass() != individualClass) {
			logger.log(Level.SEVERE,
					"Individual doesn't contain right type.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Verifies if problem is instance of problemClass
	 * @param problem
	 * @param problemClass
	 * @param logger
	 * @return
	 */
	public static boolean isProblemTypeOf(Problem problem,
			Class<?> problemClass, AgentLogger logger) {
		
		if (problem.getClass() != problemClass) {
			logger.log(Level.SEVERE,
					"Problem doesn't contain right type.");
			return false;
		}
		
		return true;
	}

	/**
	 * Verifies if problemTool is instance of problemToolClass
	 * @param problemTool
	 * @param problemToolClass
	 * @param logger
	 * @return
	 */
	public static boolean isProblemToolTypeOf(ProblemTool problemTool,
			Class<?> problemToolClass, AgentLogger logger) {
		
		boolean isProblemToolOK =
				problemTool.getClass().isAssignableFrom(problemToolClass);
		
		if (! isProblemToolOK) {
			logger.log(Level.SEVERE,
					"ProblemTool doesn't contain right type.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates instance of ProblemTool from string className
	 * @param className
	 * @param logger
	 * @return
	 */
	public static ProblemTool instanceProblemTool(String className,
			AgentLogger logger) {
		
		Class<?> toolClass = null;
		try {
			toolClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.logThrowable(
					"Class of problemTool was not found", e);
		}
		
		ProblemTool problemTool = null;
		try {
			problemTool = (ProblemTool) toolClass.newInstance();
		} catch (InstantiationException e) {
			logger.logThrowable(
					"Class of problemTool can't be instanced", e);
		} catch (IllegalAccessException e) {
			logger.logThrowable(
					"Class of problemTool can't be instanced", e);
		}

		return problemTool;
	}
	
}
