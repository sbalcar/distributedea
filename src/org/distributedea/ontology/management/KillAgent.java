package org.distributedea.ontology.management;

import jade.content.AgentAction;

public class KillAgent implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private String agentName;

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
}
