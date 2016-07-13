package org.distributedea.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;

public class AgentConfigurations {
	
	private List<AgentConfiguration> agentConfigurations;

	public AgentConfigurations() {
	}
	
	public AgentConfigurations(List<AgentConfiguration> agentConfigurations) {
		this.agentConfigurations = agentConfigurations;
	}
	public AgentConfigurations(AgentConfigurations agentConfigurationsStruct) {
		
		for (AgentConfiguration agentConfI : agentConfigurationsStruct.getAgentConfigurations()) {
			
			AgentConfiguration agentConfCloneI = new AgentConfiguration(agentConfI);
			
			addAgentConfigurations(agentConfCloneI);
		}
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
	
	public boolean valid(IAgentLogger logger) {
		
		if (agentConfigurations.isEmpty()) {
			logger.log(Level.INFO, "Any agent-method available");
			return false;
		}
		
		return true;
	}
	
	public AgentConfigurations deepClone() {
		return new AgentConfigurations(this);
	}
}
