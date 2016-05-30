package org.distributedea.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;

public class AgentConfigurations {
	
	private List<AgentConfiguration> agentConfigurations;

	public AgentConfigurations() {
	}
	
	public AgentConfigurations(List<AgentConfiguration> agentConfigurations) {
		this.agentConfigurations = agentConfigurations;
	}
	public List<AgentConfiguration> getAgentConfigurations() {
		return agentConfigurations;
	}
	public void addAgentConfigurations(AgentConfiguration agentConfiguration) {
		
		if (this.agentConfigurations == null) {
			agentConfigurations = new ArrayList<>();
		}
		agentConfigurations.add(agentConfiguration);
	}
	public AgentConfiguration exportAgentConfigurations(int index) {
		
		if (this.agentConfigurations == null) {
			return null;
		}
		return agentConfigurations.get(index);
	}
	
	public boolean valid(AgentLogger logger) {
		
		if (agentConfigurations.isEmpty()) {
			logger.log(Level.INFO, "Any agent-method available");
			return false;
		}
		
		return true;
	}
}
