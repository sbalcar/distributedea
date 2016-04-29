package org.distributedea.ontology.job;

import java.util.List;

import jade.content.Concept;

public class Job implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Declares Job IDentification
	 */
	private String jobID;
	
	/**
	 * Inform about type of Problem to solve
	 */
	private Class<?> problemToSolve;
	
	/**
	 * Defines the filename with the input Problem
	 */
	private String problemFileName;
	
	/**
	 * Defines the filename with method(agent types)
	 */
	private String methodsFileName;
	
	/**
	 * Declares the set of available ProblemTools for Computing Agents
	 */
	private List<Class<?>> availableProblemTools;
	
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Declares the Scheduler Class which will be used to direction of the evolution
	 */
	private Class<?> scheduler;
	
	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}	
	
	public Class<?> getProblemToSolve() {
		return problemToSolve;
	}
	public void setProblemToSolve(Class<?> problemToSolve) {
		this.problemToSolve = problemToSolve;
	}
	
	public String getProblemFileName() {
		return problemFileName;
	}
	public void setProblemFileName(String problemFileName) {
		this.problemFileName = problemFileName;
	}
	
	public String getMethodsFileName() {
		return methodsFileName;
	}
	public void setMethodsFileName(String methodsFileName) {
		this.methodsFileName = methodsFileName;
	}
	
	public List<Class<?>> getAvailableProblemTools() {
		return availableProblemTools;
	}
	public void setAvailableProblemTools(List<Class<?>> availableProblemTools) {
		this.availableProblemTools = availableProblemTools;
	}

	public boolean isIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	public Class<?> getScheduler() {
		return scheduler;
	}
	public void setScheduler(Class<?> scheduler) {
		this.scheduler = scheduler;
	}
	
	
}
