package org.distributedea.ontology.management;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;

import jade.content.Concept;

/**
 * Ontology represents information about created agent.
 * @author stepan
 *
 */
public class CreatedAgent implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentConfiguration createdAgent;
	
	
	@Deprecated
	public CreatedAgent() {} // only for Jade
	
	/**
	 * Constructor
	 * @param createdAgent
	 */
	public CreatedAgent(AgentConfiguration createdAgent) {
		if (createdAgent == null || ! createdAgent.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.createdAgent = createdAgent;
	}
	
	public AgentConfiguration getCreatedAgent() {
		return createdAgent;
	}
	@Deprecated
	public void setCreatedAgent(AgentConfiguration createdAgent) {
		if (createdAgent == null || ! createdAgent.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.createdAgent = createdAgent;
	}

}
