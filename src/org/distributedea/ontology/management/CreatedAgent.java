package org.distributedea.ontology.management;

import org.distributedea.ontology.configuration.AgentConfiguration;

import jade.content.Concept;

public class CreatedAgent implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentConfiguration createdAgent;
	
	public AgentConfiguration getCreatedAgent() {
		return createdAgent;
	}
	public void setCreatedAgent(AgentConfiguration createdAgent) {
		this.createdAgent = createdAgent;
	}

}
