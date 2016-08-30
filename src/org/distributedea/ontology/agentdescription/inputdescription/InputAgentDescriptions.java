package org.distributedea.ontology.agentdescription.inputdescription;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;

/**
 * Ontology represents {@link List} of {@link InputAgentDescription} elements.
 * @author stepan
 *
 */
public class InputAgentDescriptions implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private List<InputAgentDescription> agentDescriptions;

	/**
	 * Constructor
	 */
	public InputAgentDescriptions() {
		this.agentDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentDescriptions
	 */
	public InputAgentDescriptions(List<InputAgentDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Copy constructor
	 * @param agentDescription
	 */
	public InputAgentDescriptions(InputAgentDescriptions agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentDescriptions.class.getSimpleName() + " is not valid");
		}
		InputAgentDescriptions agentDescriptionClone = agentDescription.deepClone();
		this.agentDescriptions = agentDescriptionClone.getAgentDescriptions();
	}

	/**
	 * Returns list of {@link InputAgentDescription}.
	 * @return
	 */
	public List<InputAgentDescription> getAgentDescriptions() {
		return agentDescriptions;
	}
	@Deprecated
	public void setAgentDescriptions(List<InputAgentDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Adds {@link InputAgentDescription}
	 * @param agentDescriptions
	 */
	public void addAgentDescriptions(InputAgentDescription agentDescriptions) {
		List<InputAgentDescription> importAgent = new ArrayList<>();
		importAgent.add(agentDescriptions);
		importDescriptions(importAgent);
	}

	private void importDescriptions(List<InputAgentDescription> agentDescriptions) {
		if (agentDescriptions == null) {
			throw new IllegalArgumentException();
		}
		IAgentLogger logger = new TrashLogger();
		for (InputAgentDescription agentDescriptionI : agentDescriptions) {
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
		return agentDescriptions.contains(agentDescription);
	}
	
	/**
	 * Removes {@link InputAgentDescription} in the specific position
	 * @param index
	 * @return
	 */
	public InputAgentDescription remove(int index) {
		
		if (index < 0) {
			throw new IllegalArgumentException("Argument " +
					int.class.getSimpleName() + " is not valid");
		}
		return this.agentDescriptions.remove(index);
	}

	/**
	 * Returns true if this structure contains no {@link AgentDescription}
	 * @return
	 */
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
		for (InputAgentDescription agentDescriptionI : agentDescriptions) {
			if (agentDescriptionI == null ||
					! agentDescriptionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
	public InputAgentDescriptions deepClone() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		List<InputAgentDescription> descriptionsClone = new ArrayList<>();
		for (InputAgentDescription agentDescriptionI : agentDescriptions) {
			descriptionsClone.add(
					agentDescriptionI.deepClone());
		}
		return new InputAgentDescriptions(descriptionsClone);
	}
}
