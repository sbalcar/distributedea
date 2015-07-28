package org.distributedea.configuration;

import java.util.List;

public class AgentConfigurations {
	
	private List<AgentConfiguration> agentConfigurations;

	public AgentConfigurations(List<AgentConfiguration> agentConfigurations) {
		this.agentConfigurations = agentConfigurations;
	}

	public List<AgentConfiguration> getAgentConfigurations() {
		return agentConfigurations;
	}
}
