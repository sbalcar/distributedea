package org.distributedea.ontology.agentdescription;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

/**
 * Ontology represents one Computing Agent helper and his priority(quality)
 * @author stepan
 *
 */
public class AgentDescriptionPriority implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentDescription description;
	private int priority;
	
	@Deprecated
	public AgentDescriptionPriority() {} //only for Jade

	/**
	 * Constructor
	 * @param description
	 * @param priority
	 */
	public AgentDescriptionPriority(AgentDescription description, int priority) {
		if (description == null || ! description.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.description = description;
		this.priority = priority;
	}
	
	/**
	 * Returns method
	 * @return
	 */
	public AgentDescription getDescription() {
		return description;
	}
	@Deprecated
	public void setDescription(AgentDescription gescription) {
		this.description = gescription;
	}
	
	/**
	 * Returns priority of method
	 * @return
	 */
	public int getPriority() {
		return priority;
	}
	@Deprecated
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (description == null || ! description.valid(logger)) {
			return false;
		}
		return true;
	}
}
