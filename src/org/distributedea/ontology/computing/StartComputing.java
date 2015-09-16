package org.distributedea.ontology.computing;

import org.distributedea.ontology.problem.Problem;

import jade.content.Concept;

public class StartComputing implements Concept {

	private static final long serialVersionUID = 1L;

	private Problem problem;

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
}