package org.distributedea.ontology.helpmate;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;

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
	private List<MethodDescriptionNumber> helpersDescriptions;

	
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
			List<MethodDescriptionNumber> helpersDescriptions) {
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

	public List<MethodDescriptionNumber> getHelpersDescriptions() {
		if (helpersDescriptions == null) {
			return new ArrayList<MethodDescriptionNumber>();
		}
		return helpersDescriptions;
	}
	@Deprecated
	public void setHelpersDescriptions(List<MethodDescriptionNumber> descriptions) {
		this.helpersDescriptions = descriptions;
	} 
	
	public void addHelperDescription(MethodDescriptionNumber agentDescriptionWrapper) {
		if (agentDescriptionWrapper == null || ! agentDescriptionWrapper.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		if (helpersDescriptions == null) {
			this.helpersDescriptions = new ArrayList<MethodDescriptionNumber>();
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
		for (MethodDescriptionNumber descriptionI : helpersDescriptions) {
			if (! descriptionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
}
