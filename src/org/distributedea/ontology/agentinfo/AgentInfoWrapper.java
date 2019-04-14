package org.distributedea.ontology.agentinfo;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;

import jade.content.Concept;

/**
 * Ontology represents {@link AgentInfo} of one concrete Agent
 * @author stepan
 *
 */
public class AgentInfoWrapper implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentInfo agentInfo;
	
	private AgentConfiguration agentConfiguration;

	@Deprecated
	public AgentInfoWrapper() {} // only for Jade
	
	/**
	 * Constructor
	 * @param agentInfo
	 * @param agentConfiguration
	 */
	public AgentInfoWrapper(AgentInfo agentInfo,
			AgentConfiguration agentConfiguration) {
		if (agentInfo == null || ! agentInfo.valid()) {
			throw new IllegalArgumentException();
		}
		if (agentConfiguration == null || ! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.agentInfo = agentInfo;
		this.agentConfiguration = agentConfiguration;
	}
	
	public AgentInfo getAgentInfo() {
		return agentInfo;
	}
	@Deprecated
	public void setAgentInfo(AgentInfo agentInfo) {
		this.agentInfo = agentInfo;
	}

	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	@Deprecated
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		this.agentConfiguration = agentConfiguration;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (agentInfo == null || ! agentInfo.valid()) {
			return false;
		}
		if (agentConfiguration == null || ! agentConfiguration.valid(logger)) {
			return false;
		}
		return true;
	}
}
