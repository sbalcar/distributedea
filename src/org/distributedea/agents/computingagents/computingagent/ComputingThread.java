package org.distributedea.agents.computingagents.computingagent;

import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.exceptions.ProblemToolException;

public class ComputingThread extends Thread {

	private Agent_ComputingAgent agent;
	private Problem problem;
	private boolean isComputing;
	
	public ComputingThread(Agent_ComputingAgent agent, Problem problem) {
		this.agent = agent;
		this.problem = problem;
		this.isComputing = true;
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
	
	@Override
	public void run() {
		
		try {
			agent.startComputing(problem, null);
		} catch (ProblemToolException e) {
			this.agent.getLogger().logThrowable("Error in the ProblemTool", e);
			this.agent.commitSuicide();
		}
		
	}}

