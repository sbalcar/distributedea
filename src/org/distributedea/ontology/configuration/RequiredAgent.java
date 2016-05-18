package org.distributedea.ontology.configuration;

import jade.content.Concept;

public class RequiredAgent implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentConfiguration agentConfiguration;

	
	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		this.agentConfiguration = agentConfiguration;
	}
	
}
