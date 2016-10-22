package org.distributedea.ontology.agentdescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;

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
	public void addAgentDescriptions(AgentDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescription.class.getSimpleName() + " is not valid");
		}
		
		agentDescriptions.add(agentDescription);
	}

	private void importDescriptions(List<AgentDescription> agentDescriptions) {
		if (agentDescriptions == null) {
			throw new IllegalArgumentException();
		}
		IAgentLogger logger = new TrashLogger();
		for (AgentDescription agentDescriptionI : agentDescriptions) {
			if (agentDescriptionI == null ||
					! agentDescriptionI.valid(logger)) {
				throw new IllegalArgumentException();
			}
		}
		this.agentDescriptions = agentDescriptions;
	}
	
	/**
	 * Exports random {@link AgentDescription}
	 * @return
	 */
	public AgentDescription exportRandomAgentDescription() {
		
		if (agentDescriptions.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int indexAD = ran.nextInt(agentDescriptions.size());
		return agentDescriptions.get(indexAD);
	}
	
	/**
	 * Exports {@link InputAgentDescriptions}
	 * @return
	 */
	public InputAgentDescriptions exportInputAgentDescriptions() {
		
		List<InputAgentDescription> aDescriptions = new ArrayList<>();
		
		for (AgentDescription agentDescriptionI : agentDescriptions) {
			InputAgentDescription inputAgentDescriptionI =
					agentDescriptionI.exportInputAgentDescription();
			aDescriptions.add(inputAgentDescriptionI);
		}
		
		return new InputAgentDescriptions(aDescriptions);
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

	/**
	 * Exports {@link AgentDescription} based of given {@link InputAgentDescription}.
	 * @param inputAgentDescription
	 * @return
	 */
	public AgentDescription exportAgentDescription(InputAgentDescription inputAgentDescription) {
		if (inputAgentDescription == null ||
				! inputAgentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					inputAgentDescription.getClass().getSimpleName() + " is not valid.");
		}
		for (AgentDescription agentDescriptionI : this.agentDescriptions) {
			InputAgentDescription inputAgentDescriptionI =
					agentDescriptionI.exportInputAgentDescription();
			if (inputAgentDescriptionI.equals(inputAgentDescription)) {
				return agentDescriptionI;
			}
		}
		return null;
	}

	public boolean remove(AgentDescription agentDescription) {
		return this.agentDescriptions.remove(agentDescription);
	}
	
	public void removeAll(AgentDescriptions agentDescriptions) {
		this.agentDescriptions.removeAll(
				agentDescriptions.getAgentDescriptions());
	}

	public void removeAll(InputAgentDescriptions inputAgentDescriptions) {
				
		for (InputAgentDescription inputAgentDescriptionI :
				inputAgentDescriptions.getInputAgentDescriptions()) {
			
			AgentDescription agentDescriptionI =
					exportAgentDescription(inputAgentDescriptionI);
			remove(agentDescriptionI);
		}
	}

	public AgentDescription export(int index) {
		return this.agentDescriptions.get(index);
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
