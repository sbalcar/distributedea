package org.distributedea.problems;

import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;

public class ProblemToolValidation {

	public static boolean isIndividualTypeOf(Individual individual,
			Class<?> individualClass, AgentLogger logger) {
			
		//if (individual.getClass().isAssignableFrom(individualClass)) {}
		
		if (individual.getClass() != individualClass) {
			logger.log(Level.SEVERE, "Individual doesn't contain right type.");
			return false;
		}
		
		return true;
	}
	
	public static boolean isProblemTypeOf(Problem problem,
			Class<?> problemClass, AgentLogger logger) {
		
		if (problem.getClass() != problemClass) {
			logger.log(Level.SEVERE, "Problem doesn't contain right type.");
			return false;
		}
		
		return true;
	}

	public static boolean isProblemToolTypeOf(ProblemTool problemTool,
			Class<?> problemToolClass, AgentLogger logger) {
		
		boolean isProblemToolOK = problemTool.getClass().isAssignableFrom(problemToolClass);
		
		if (! isProblemToolOK) {
			logger.log(Level.SEVERE, "ProblemTool doesn't contain right type.");
			return false;
		}
		
		return true;
	}
	
	public static ProblemTool instanceProblemTool(String className, AgentLogger logger) {
		
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
