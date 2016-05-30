package org.distributedea.ontology.job;


import jade.content.Concept;

import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;

public class JobRun implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Job identification
	 */
	private JobID jobID;
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Agent configurations - available for problem solving
	 */
	private AgentConfigurations agentConfigurations;
	
	/**
	 * Problem Tools to use for solving Problem 
	 */
	private ProblemTools problemTools;
	
	/**
	 * File with problem
	 */
	private Problem problem;

	
	public JobID getJobID() {
		return jobID;
	}
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}
	
	
	public AgentConfigurations getAgentConfigurations() {
		return agentConfigurations;
	}
	public void setAgentConfigurations(AgentConfigurations agentConfigurations) {
		this.agentConfigurations = agentConfigurations;
	}
	
	
	public boolean getIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	
	public ProblemTools getProblemTools() {
		return problemTools;
	}
	public void setProblemTools(ProblemTools problemTools) {
		this.problemTools = problemTools;
	}

	
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	/**
	 * Exports the Job as ProblemStruct which contains ProblemTool in argument
	 * @param problemToolClass
	 * @return
	 */
	public ProblemStruct exportProblemStruct(Class<?> problemToolClass) {
		
		JobID jobIDCopy = new JobID();
		jobIDCopy.setBatchID(getJobID().getBatchID());
		jobIDCopy.setJobID(getJobID().getJobID());
		jobIDCopy.setRunNumber(getJobID().getRunNumber());
		
		ProblemStruct problemStruct = new ProblemStruct();
		problemStruct.setJobID(jobIDCopy);
		problemStruct.setIndividualDistribution(getIndividualDistribution());
		problemStruct.setProblem(getProblem());
		problemStruct.importProblemToolClass(problemToolClass);
		
		return problemStruct;
	}
	
	public boolean validation() {
		return true;
	}
}
