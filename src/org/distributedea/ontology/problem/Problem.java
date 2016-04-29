package org.distributedea.ontology.problem;

import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolValidation;

import jade.content.Concept;

/**
 * Ontology which represents Problem to solve
 */
public abstract class Problem implements Concept {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private String problemID;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;

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
	public abstract boolean testIsValid(Individual individual, AgentLogger logger);
	
	
	/**
	 * get Problem IDentification
	 * @return
	 */
	public String getProblemID() {
		return problemID;
	}

	/**
	 * set Problem IDentification
	 * @param problemID
	 */
	public void setProblemID(String problemID) {
		this.problemID = problemID;
	}

	/**
	 * get Problem Tool
	 * @return
	 */
	public String getProblemToolClass() {
		return problemToolClass;
	}

	/**
	 * set Problem Tool
	 * @param problemToolClass
	 */
	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}

	/**
	 * Tests validation of the Problem Ontology
	 * @param logger
	 * @return
	 */
	public boolean testIsValid(AgentLogger logger) {
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				getProblemToolClass(), logger);
		
		if ( this.getClass() == problemTool.problemWhichSolves() ) {
			return true;
		}
		
		logger.log(Level.SEVERE, "Problem is not valid");
		
		return false;
	}
	
}