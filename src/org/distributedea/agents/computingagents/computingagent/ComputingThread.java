package org.distributedea.agents.computingagents.computingagent;

import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.exceptions.ProblemToolException;

public class ComputingThread extends Thread {

	private Agent_ComputingAgent agent;
	
	private String jobID;
	private Class<?> problemToolClass;
	private Problem problem;
	private boolean isComputing;
	
	/** best result of computing (Individual and fitness) **/
	private ResultOfComputing bestResultOfComputing = null;
	
	public ComputingThread(Agent_ComputingAgent agent, ProblemStruct problemStruct) {
		this.agent = agent;
		this.jobID = problemStruct.getJobID();
		this.problemToolClass = problemStruct.exportProblemToolClass(agent.getLogger());
		this.problem = problemStruct.getProblem();
		this.isComputing = true;
	}
	
	public String getJobID() {
		return this.jobID;
	}
	public Class<?> getProblemTool() {
		return this.problemToolClass;
	}
	public Problem getProblem() {
		return this.problem;
	}
	
	public boolean continueInTheNextGeneration() {
		return this.isComputing;
	}

	public void stopComputing() {
		this.isComputing = false;
	}
	
	public ResultOfComputing getBestresultOfComputing() {
		
		return bestResultOfComputing;
	}
	public void setBestresultOfComputing(ResultOfComputing resultOfComputing) {
		
		this.bestResultOfComputing = resultOfComputing;
		
		if (resultOfComputing != null) {
			resultOfComputing.setJobID(jobID);
		}
	}
	
	
	@Override
	public void run() {
		
		try {
			agent.startComputing(problem, problemToolClass, jobID, null);
		} catch (ProblemToolException e) {
			this.agent.getLogger().logThrowable("Error in the ProblemTool", e);
			this.agent.commitSuicide();
		}
		
	}}

