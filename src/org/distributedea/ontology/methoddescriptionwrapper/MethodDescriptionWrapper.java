package org.distributedea.ontology.methoddescriptionwrapper;

import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.methoddescription.MethodDescription;

import jade.content.Concept;

public class MethodDescriptionWrapper implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodDescription methodDescription;
	
	private AgentConfiguration agentConfiguration;

	
	public MethodDescription getMethodDescription() {
		return methodDescription;
	}
	public void setMethodDescription(MethodDescription methodDescription) {
		this.methodDescription = methodDescription;
	}

	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		this.agentConfiguration = agentConfiguration;
	}
	
}
