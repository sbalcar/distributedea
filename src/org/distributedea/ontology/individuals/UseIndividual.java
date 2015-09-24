package org.distributedea.ontology.individuals;

import jade.content.AgentAction;

public class UseIndividual implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private Individual individual;

	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

}
