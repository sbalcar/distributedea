package org.distributedea.ontology.computing;

import org.distributedea.ontology.problemwrapper.ProblemWrapper;

import jade.content.AgentAction;

public class StartComputing implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ProblemWrapper problemWrapper;

	public ProblemWrapper getProblemWrapper() {
		return problemWrapper;
	}

	public void setProblemWrapper(ProblemWrapper problemWrapper) {
		this.problemWrapper = problemWrapper;
	}
	
}