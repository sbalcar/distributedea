package org.distributedea.ontology.configuration;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

/**
 * Ontology represents required {@link AgentConfiguration}
 * @author stepan
 *
 */
public class RequiredAgent implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentConfiguration agentConfiguration;

	@Deprecated
	public RequiredAgent() {} //only for Jade
	
	/**
	 * Constructor
	 * @param agentConfiguration
	 */
	public RequiredAgent(AgentConfiguration agentConfiguration) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.agentConfiguration = agentConfiguration;
	}
	
	
	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	@Deprecated
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.agentConfiguration = agentConfiguration;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (agentConfiguration == null || ! agentConfiguration.valid(logger)) {
			return false;
		}
		return true;
	}
}
