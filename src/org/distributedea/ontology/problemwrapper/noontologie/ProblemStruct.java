package org.distributedea.ontology.problemwrapper.noontologie;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolValidation;

public class ProblemStruct {

	/**
	 * Job identification
	 */
	private String jobID;
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;
	
	/**
	 * File with problem
	 */
	private Problem problem;

	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	
	public boolean getIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	
	public String getProblemToolClass() {
		return problemToolClass;
	}
	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}
	public Class<?> exportProblemToolClass(AgentLogger logger) {
		
		try {
			return Class.forName(getProblemToolClass());
		} catch (ClassNotFoundException e) {
			logger.logThrowable("Can not find class for ProblemToll", e);
		}
		return null;
	}

	
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	
	public boolean testIsValid(AgentLogger logger) {
		
		ProblemTool problemTool = ProblemToolValidation.instanceProblemTool(
				getProblemToolClass(), logger);
		
		Class<?> problemClass = problemTool.problemWhichSolves();
		// if problemTool can solve the problem
		if (! getProblem().getClass().equals(problemClass)) {
			return false;
		}
		
		return true;
	}
	
}
