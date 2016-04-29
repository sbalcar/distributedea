package org.distributedea.ontology.agentdescription;

import jade.content.Concept;

public class AgentDescriptionWrapper implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentDescription description;
	private int priority;
	
	
	public AgentDescription getDescription() {
		return description;
	}
	public void setDescription(AgentDescription gescription) {
		this.description = gescription;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
