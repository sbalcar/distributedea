package org.distributedea.ontology.problemwrapper;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemTool;

import jade.content.Concept;

public class ProblemWrapper implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Job identification
	 */
	private String jobID;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;
	
	/**
	 * File with problem
	 */
	private String problemFileName;
	
	/**
	 * get Problem IDentification
	 * @return
	 */
	public String getJobID() {
		return jobID;
	}
	/**
	 * set Problem IDentification
	 * @param problemID
	 */
	public void setJobID(String jobID) {
		this.jobID = jobID;
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
	public void importProblemToolClass(Class<?> problemToolClass) {
		this.problemToolClass = problemToolClass.getName();
	}
	public Class<?> exportProblemToolClass() {
		try {
			return Class.forName(problemToolClass);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public String getProblemFileName() {
		return problemFileName;
	}
	public void setProblemFileName(String problemFileName) {
		this.problemFileName = problemFileName;
	}
	
	
	public boolean testIsValid(AgentLogger logger) {
		return true;
	}
	
	public Problem exportProblem(AgentLogger logger) {
		
		ProblemTool problemTool = null;
		try {
			problemTool = (ProblemTool) Class.forName(problemToolClass).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			throw new IllegalStateException("Can not instance problem Tool");
		} 
		
		Problem problem = problemTool.readProblem(getProblemFileName(), logger);
		
		return problem;
	}
	
	public ProblemStruct exportProblemStruct(AgentLogger logger) {
		
		ProblemStruct struct = new ProblemStruct();
		struct.setJobID(getJobID());
		struct.setProblemToolClass(getProblemToolClass());
		struct.setProblem(exportProblem(logger));
		
		return struct;
	}
	
}
