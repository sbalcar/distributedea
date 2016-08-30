package org.distributedea.ontology.agentdescription;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

/**
 * Ontology represents list of {@link AgentDescription}.
 * @author stepan
 *
 */
public class AgentDescriptions implements Concept {

	private static final long serialVersionUID = 1L;

	private List<AgentDescription> agentDescriptions;
	
	/**
	 * Constructor
	 */
	public AgentDescriptions() {
		this.agentDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentDescriptions
	 */
	public AgentDescriptions(List<AgentDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Copy constructor
	 * @param agentDescription
	 */
	public AgentDescriptions(AgentDescriptions agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		AgentDescriptions agentDescriptionClone = agentDescription.deepClone();
		this.agentDescriptions = agentDescriptionClone.getAgentDescriptions();
	}

	/**
	 * Returns list of {@link AgentDescription}.
	 * @return
	 */
	public List<AgentDescription> getAgentDescriptions() {
		return agentDescriptions;
	}
	@Deprecated
	public void setAgentDescriptions(List<AgentDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Adds {@link AgentDescription}
	 * @param agentDescriptions
	 */
	public void addAgentDescriptions(AgentDescription agentDescriptions) {
		List<AgentDescription> importAgent = new ArrayList<>();
		importAgent.add(agentDescriptions);
		importDescriptions(importAgent);
	}

	private void importDescriptions(List<AgentDescription> agentDescriptions) {
		if (agentDescriptions == null) {
			throw new IllegalArgumentException();
		}
		IAgentLogger logger = new TrashLogger();
		for (AgentDescription agentDescriptionI : agentDescriptions) {
			if (agentDescriptionI == null ||
					! agentDescriptionI.valid(logger)) {
				agentDescriptionI.valid(logger);
				throw new IllegalArgumentException();
			}
		}
		this.agentDescriptions = agentDescriptions;
	}
	
	/**
	 * Tests if this {@link AgentDescriptions} contains given {@link AgentDescription}.
	 * @param agentDescription
	 * @return
	 */
	public boolean containsAgentDescription(AgentDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					agentDescription.getClass().getSimpleName() + " is not valid.");
		}
		for (AgentDescription agentDescriptionI : this.agentDescriptions) {
			if (agentDescriptionI.equals(agentDescription)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeAll(AgentDescriptions agentDescriptions) {
		this.agentDescriptions.removeAll(
				agentDescriptions.getAgentDescriptions());
	}
	
	public boolean isEmpty() {
		return this.agentDescriptions.isEmpty();
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (agentDescriptions == null) {
			return false;
		}
		for (AgentDescription agentDescriptionI : agentDescriptions) {
			if (agentDescriptionI == null ||
					! agentDescriptionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public AgentDescriptions deepClone() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		List<AgentDescription> descriptionsClone = new ArrayList<>();
		for (AgentDescription agentDescriptionI : agentDescriptions) {
			descriptionsClone.add(
					agentDescriptionI.deepClone());
		}
		return new AgentDescriptions(descriptionsClone);
	}
}
