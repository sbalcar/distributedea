package org.distributedea.ontology.helpmate;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptionWrapper;

import jade.content.Concept;

public class HelpmateList implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Description of Computing Agent
	 */
	private AgentDescription description;
	
	/**
	 * Descriptions of the helpers
	 */
	private List<AgentDescriptionWrapper> descriptions;

	
	public AgentDescription getDescription() {
		return description;
	}
	public void setDescription(AgentDescription description) {
		this.description = description;
	}
	
	public List<AgentDescriptionWrapper> getDescriptions() {
		if (descriptions == null) {
			return new ArrayList<AgentDescriptionWrapper>();
		}
		return descriptions;
	}
	public void setDescriptions(List<AgentDescriptionWrapper> descriptions) {
		this.descriptions = descriptions;
	} 
	
	public void addDescription(AgentDescriptionWrapper agentDescriptionWrapper) {
		
		if (descriptions == null) {
			this.descriptions = new ArrayList<AgentDescriptionWrapper>();
		}
		descriptions.add(agentDescriptionWrapper);
	}
}
