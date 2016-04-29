package org.distributedea.ontology.management;

import org.distributedea.ontology.configuration.AgentConfiguration;

import jade.content.AgentAction;

public class CreateAgent implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private AgentConfiguration configuration;

	
	public AgentConfiguration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(AgentConfiguration configuration) {
		this.configuration = configuration;
	}
	
}
