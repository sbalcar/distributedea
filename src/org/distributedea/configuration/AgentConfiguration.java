package org.distributedea.configuration;

import java.util.List;

import org.distributedea.ontology.management.agent.Argument;


public class AgentConfiguration {
	
	private String agentName;
	private String agentType;
	private List<Argument> arguments;

	public AgentConfiguration(String agentName, String agentType,
			List<Argument> arguments) {
		
		this.agentName = agentName;
		this.agentType = agentType;
		this.arguments = arguments;
	}

	public String getAgentName() {
		return agentName;
	}

	public String getAgentType() {
		return agentType;
	}

	public List<Argument> getArguments() {
		return arguments;
	}
}
