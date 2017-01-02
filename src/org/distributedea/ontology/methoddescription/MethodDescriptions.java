package org.distributedea.ontology.methoddescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;

import jade.content.Concept;

/**
 * Ontology represents list of {@link MethodDescription}.
 * @author stepan
 *
 */
public class MethodDescriptions implements Concept {

	private static final long serialVersionUID = 1L;

	private List<MethodDescription> agentDescriptions;
	
	/**
	 * Constructor
	 */
	public MethodDescriptions() {
		this.agentDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentDescriptions
	 */
	public MethodDescriptions(List<MethodDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Copy constructor
	 * @param agentDescription
	 */
	public MethodDescriptions(MethodDescriptions agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		MethodDescriptions agentDescriptionClone = agentDescription.deepClone();
		this.agentDescriptions = agentDescriptionClone.getAgentDescriptions();
	}

	/**
	 * Returns list of {@link MethodDescription}.
	 * @return
	 */
	public List<MethodDescription> getAgentDescriptions() {
		return agentDescriptions;
	}
	@Deprecated
	public void setAgentDescriptions(List<MethodDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Adds {@link MethodDescription}
	 * @param agentDescriptions
	 */
	public void addAgentDescriptions(MethodDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		
		agentDescriptions.add(agentDescription);
	}

	private void importDescriptions(List<MethodDescription> agentDescriptions) {
		if (agentDescriptions == null) {
			throw new IllegalArgumentException();
		}
		IAgentLogger logger = new TrashLogger();
		for (MethodDescription agentDescriptionI : agentDescriptions) {
			if (agentDescriptionI == null ||
					! agentDescriptionI.valid(logger)) {
				throw new IllegalArgumentException();
			}
		}
		this.agentDescriptions = agentDescriptions;
	}
	
	/**
	 * Exports random {@link MethodDescription}
	 * @return
	 */
	public MethodDescription exportRandomAgentDescription() {
		
		if (agentDescriptions.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int indexAD = ran.nextInt(agentDescriptions.size());
		return agentDescriptions.get(indexAD);
	}
	
	/**
	 * Exports {@link InputMethodDescriptions}
	 * @return
	 */
	public InputMethodDescriptions exportInputAgentDescriptions() {
		
		List<InputMethodDescription> aDescriptions = new ArrayList<>();
		
		for (MethodDescription agentDescriptionI : agentDescriptions) {
			InputMethodDescription inputAgentDescriptionI =
					agentDescriptionI.exportInputAgentDescription();
			aDescriptions.add(inputAgentDescriptionI);
		}
		
		return new InputMethodDescriptions(aDescriptions);
	}
	
	/**
	 * Tests if this {@link MethodDescriptions} contains given {@link MethodDescription}.
	 * @param agentDescription
	 * @return
	 */
	public boolean containsMethodDescription(MethodDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					agentDescription.getClass().getSimpleName() + " is not valid.");
		}
		for (MethodDescription agentDescriptionI : this.agentDescriptions) {
			if (agentDescriptionI.equals(agentDescription)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Exports {@link MethodDescription} based of given {@link InputMethodDescription}.
	 * @param inputAgentDescription
	 * @return
	 */
	public MethodDescription exportAgentDescription(InputMethodDescription inputAgentDescription) {
		if (inputAgentDescription == null ||
				! inputAgentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					inputAgentDescription.getClass().getSimpleName() + " is not valid.");
		}
		for (MethodDescription agentDescriptionI : this.agentDescriptions) {
			InputMethodDescription inputAgentDescriptionI =
					agentDescriptionI.exportInputAgentDescription();
			if (inputAgentDescriptionI.equals(inputAgentDescription)) {
				return agentDescriptionI;
			}
		}
		return null;
	}

	public boolean remove(MethodDescription agentDescription) {
		return this.agentDescriptions.remove(agentDescription);
	}
	
	public void removeAll(MethodDescriptions agentDescriptions) {
		this.agentDescriptions.removeAll(
				agentDescriptions.getAgentDescriptions());
	}

	public void removeAll(InputMethodDescriptions inputAgentDescriptions) {
				
		for (InputMethodDescription inputAgentDescriptionI :
				inputAgentDescriptions.getInputAgentDescriptions()) {
			
			MethodDescription agentDescriptionI =
					exportAgentDescription(inputAgentDescriptionI);
			remove(agentDescriptionI);
		}
	}

	public MethodDescription export(int index) {
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
		for (MethodDescription agentDescriptionI : agentDescriptions) {
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
	public MethodDescriptions deepClone() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		List<MethodDescription> descriptionsClone = new ArrayList<>();
		for (MethodDescription agentDescriptionI : agentDescriptions) {
			descriptionsClone.add(
					agentDescriptionI.deepClone());
		}
		return new MethodDescriptions(descriptionsClone);
	}
}
