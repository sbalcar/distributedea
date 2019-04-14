package org.distributedea.ontology.agentinfo;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.agentconfiguration.AgentConfigurations;

/**
 * Data structures for list of {@link AgentInfoWrapper}
 * @author stepan
 *
 */
public class AgentInfosWrapper {

	private List<AgentInfoWrapper> agentInfoWrappers;

	@Deprecated
	public AgentInfosWrapper() { // only for Jade
		this.agentInfoWrappers = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentInfoWrappers
	 */
	public AgentInfosWrapper(List<AgentInfoWrapper> agentInfoWrappers) {
		if (agentInfoWrappers == null) {
			throw new IllegalArgumentException();
		}
		for (AgentInfoWrapper agentInfoI : agentInfoWrappers) {
			if (! agentInfoI.valid(new TrashLogger())) {
				throw new IllegalArgumentException();
			}
		}
		this.agentInfoWrappers = agentInfoWrappers;
	}
	
	public List<AgentInfoWrapper> getAgentInfoWrappers() {
		return agentInfoWrappers;
	}
	@Deprecated
	public void setAgentInfoWrappers(List<AgentInfoWrapper> agentInfoWrappers) {
		this.agentInfoWrappers = agentInfoWrappers;
	}

	public void addMethodDescriptionWrapper(AgentInfoWrapper agentInfoWrp) {
		if (agentInfoWrp == null || ! agentInfoWrp.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		if (this.agentInfoWrappers == null) {
			this.agentInfoWrappers = new ArrayList<>();
		}
		this.agentInfoWrappers.add(agentInfoWrp);
	}
	
	public AgentInfosWrapper exportExploitationAgentInfos() {
		
		AgentInfosWrapper exploitationMethods = new AgentInfosWrapper();
		
		for (AgentInfoWrapper agentInfoWrpI : agentInfoWrappers) {
			
			AgentInfo agentInfoI = agentInfoWrpI.getAgentInfo();
			
			if (agentInfoI.isExploitation()) {
				exploitationMethods.addMethodDescriptionWrapper(agentInfoWrpI);
			}
		}
		
		return exploitationMethods;
	}
	
	public AgentInfosWrapper exportExplorationAgentInfos() {
		
		AgentInfosWrapper explorationMethods = new AgentInfosWrapper();
		
		for (AgentInfoWrapper agentInfoWrpI : agentInfoWrappers) {
			
			AgentInfo agentInfoI = agentInfoWrpI.getAgentInfo();
			
			if (agentInfoI.isExploration()) {
				explorationMethods.addMethodDescriptionWrapper(agentInfoWrpI);
			}
		}
		
		return explorationMethods;
	}
	
	public AgentConfigurations exportAgentConfigurations() {
		
		List<AgentConfiguration> configurations = new ArrayList<>();
		
		for (AgentInfoWrapper methDesWrpI : agentInfoWrappers) {
			AgentConfiguration agentConfigurationI =
					methDesWrpI.getAgentConfiguration();
			
			configurations.add(agentConfigurationI);
		}
		
		return new AgentConfigurations(configurations);
	}
	
	public List<Class<?>> exportAgentTypes() {
		
		List<Class<?>> agentClass = new ArrayList<>();
		
		List<AgentConfiguration> configurations =
				exportAgentConfigurations().getAgentConfigurations();
		for (AgentConfiguration configurationI : configurations) {
			
			Class<?> agentTypeI = configurationI.exportAgentClass();
			if (! agentClass.contains(agentTypeI)) {
				agentClass.add(agentTypeI);
			}
		}
		
		return agentClass;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (agentInfoWrappers == null) {
			return false;
		}
		for (AgentInfoWrapper agentInfoWrpI : agentInfoWrappers) {
			if (! agentInfoWrpI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
}
