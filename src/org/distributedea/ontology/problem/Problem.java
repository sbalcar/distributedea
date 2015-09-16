package org.distributedea.ontology.problem;

import jade.content.Concept;

public class Problem  implements Concept {

	private static final long serialVersionUID = 1L;
	
	private String problemToolClass;

	
	public String getProblemToolClass() {
		return problemToolClass;
	}

	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}

}