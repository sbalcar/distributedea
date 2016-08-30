package org.distributedea.ontology.helpmate;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptionPriority;

import jade.content.Concept;

/**
 * Ontology represents list of {@list AgentDescriptionWrapper}
 * @author stepan
 *
 */
public class StatisticOfHelpmates implements Concept {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Agent description
	 */
	AgentDescription description;
	
	/**
	 * Descriptions of helpers
	 */
	private List<AgentDescriptionPriority> helpersDescriptions;

	
	@Deprecated
	public StatisticOfHelpmates() { // only for Jade
		this.helpersDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param description
	 * @param helpersDescriptions
	 */
	public StatisticOfHelpmates(AgentDescription description,
			List<AgentDescriptionPriority> helpersDescriptions) {
		if (description == null || ! description.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.description = description;
		this.helpersDescriptions = helpersDescriptions;
	}
	
	
	public AgentDescription getAgentDescription() {
		return description;
	}
	@Deprecated
	public void setAgentDescription(AgentDescription description) {
		this.description = description;
	}

	public List<AgentDescriptionPriority> getHelpersDescriptions() {
		if (helpersDescriptions == null) {
			return new ArrayList<AgentDescriptionPriority>();
		}
		return helpersDescriptions;
	}
	@Deprecated
	public void setHelpersDescriptions(List<AgentDescriptionPriority> descriptions) {
		this.helpersDescriptions = descriptions;
	} 
	
	public void addHelperDescription(AgentDescriptionPriority agentDescriptionWrapper) {
		if (agentDescriptionWrapper == null || ! agentDescriptionWrapper.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		if (helpersDescriptions == null) {
			this.helpersDescriptions = new ArrayList<AgentDescriptionPriority>();
		}
		helpersDescriptions.add(agentDescriptionWrapper);
	}
	
	/**
	 * Tests validate
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (description == null || ! description.valid(logger)) {
			return false;
		}
		if (helpersDescriptions == null) {
			return false;
		}
		for (AgentDescriptionPriority descriptionI : helpersDescriptions) {
			if (! descriptionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
}
