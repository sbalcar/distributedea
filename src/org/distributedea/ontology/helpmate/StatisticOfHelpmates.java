package org.distributedea.ontology.helpmate;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounter;

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
	MethodDescription description;
	
	/**
	 * Descriptions of helpers
	 */
	private List<MethodDescriptionCounter> helpersDescriptions;

	
	@Deprecated
	public StatisticOfHelpmates() { // only for Jade
		this.helpersDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param description
	 * @param helpersDescriptions
	 */
	public StatisticOfHelpmates(MethodDescription description,
			List<MethodDescriptionCounter> helpersDescriptions) {
		if (description == null || ! description.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.description = description;
		this.helpersDescriptions = helpersDescriptions;
	}
	
	
	public MethodDescription getAgentDescription() {
		return description;
	}
	@Deprecated
	public void setAgentDescription(MethodDescription description) {
		this.description = description;
	}

	public List<MethodDescriptionCounter> getHelpersDescriptions() {
		if (helpersDescriptions == null) {
			return new ArrayList<MethodDescriptionCounter>();
		}
		return helpersDescriptions;
	}
	@Deprecated
	public void setHelpersDescriptions(List<MethodDescriptionCounter> descriptions) {
		this.helpersDescriptions = descriptions;
	} 
	
	public void addHelperDescription(MethodDescriptionCounter agentDescriptionWrapper) {
		if (agentDescriptionWrapper == null || ! agentDescriptionWrapper.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		if (helpersDescriptions == null) {
			this.helpersDescriptions = new ArrayList<MethodDescriptionCounter>();
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
		for (MethodDescriptionCounter descriptionI : helpersDescriptions) {
			if (! descriptionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
}
