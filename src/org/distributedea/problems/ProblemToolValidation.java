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
}
