package org.distributedea.ontology.agentdescription.inputdescription;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfigurations;
import org.distributedea.ontology.methodtype.MethodType;

/**
 * Ontology represents {@link List} of {@link InputAgentDescription} elements.
 * @author stepan
 *
 */
public class InputAgentDescriptions implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private List<InputAgentDescription> inputAgentDescriptions;

	/**
	 * Constructor
	 */
	public InputAgentDescriptions() {
		this.inputAgentDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentDescriptions
	 */
	public InputAgentDescriptions(List<InputAgentDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Constructor - Cartesian product of {@link InputAgentConfigurations}
	 * and {@link ProblemTools}
	 * @param configurations
	 * @param problemTools
	 */
	public InputAgentDescriptions(InputAgentConfigurations configurations,
			ProblemTools problemTools) {
		
		if (configurations == null ||
				! configurations.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfigurations.class.getSimpleName() + "is not valid");
		}
		if (problemTools == null ||
				! problemTools.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemTools.class.getSimpleName() + "is not valid");
		}
		
		List<InputAgentDescription> descriptions = new ArrayList<>();
		
		for (InputAgentConfiguration configurationI : configurations.getAgentConfigurations()) {
			
			for (Class<?> problemToolsI : problemTools.getProblemTools()) {
				
				InputAgentDescription descriptionI =
						new InputAgentDescription(configurationI, problemToolsI);
				
				descriptions.add(descriptionI);
			}
		}
		
		importDescriptions(descriptions);
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
		this.inputAgentDescriptions = agentDescriptionClone.getInputAgentDescriptions();
	}

	/**
	 * Returns list of {@link InputAgentDescription}.
	 * @return
	 */
	public List<InputAgentDescription> getInputAgentDescriptions() {
		return inputAgentDescriptions;
	}
	@Deprecated
	public void setInputAgentDescriptions(List<InputAgentDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Adds {@link InputAgentDescription}
	 * @param agentDescriptions
	 */
	public void addAgentDescriptions(InputAgentDescription agentDescriptions) {
		if (agentDescriptions == null ||
				! agentDescriptions.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentDescription.class.getSimpleName() + " is not valid");			
		}
		
		this.inputAgentDescriptions.add(agentDescriptions);
	}

	private void importDescriptions(List<InputAgentDescription> agentDescriptions) {
		if (agentDescriptions == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		IAgentLogger logger = new TrashLogger();
		for (InputAgentDescription agentDescriptionI : agentDescriptions) {
			if (agentDescriptionI == null ||
					! agentDescriptionI.valid(logger)) {
				agentDescriptionI.valid(logger);
				throw new IllegalArgumentException();
			}
		}
		this.inputAgentDescriptions = agentDescriptions;
	}
	
	/**
	 * Tests if this {@link AgentDescriptions} contains given {@link AgentDescription}.
	 * @param agentDescription
	 * @return
	 */
	public boolean containsAgentDescription(InputAgentDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					agentDescription.getClass().getSimpleName() + " is not valid.");
		}
		return inputAgentDescriptions.contains(agentDescription);
	}
	
	
	public InputAgentDescription get(int index) {
		
		return this.inputAgentDescriptions.get(index);
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
		return this.inputAgentDescriptions.remove(index);
	}

	public boolean removeAll(InputAgentDescriptions inputAgentDscrs) {
		return this.inputAgentDescriptions.removeAll(
				inputAgentDscrs.getInputAgentDescriptions());
	}
	
	/**
	 * Returns true if this structure contains no {@link AgentDescription}
	 * @return
	 */
	public boolean isEmpty() {

		return this.inputAgentDescriptions.isEmpty();
	}

	/**
	 * Returns size
	 * @return
	 */
	public int size() {

		return this.inputAgentDescriptions.size();
	}
	
	/**
	 * Exports random selected {@link InputAgentDescription}
	 * @return
	 */
	public InputAgentDescription exportRandomInputAgentDescription() {
		
		if (inputAgentDescriptions.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int indexAD = ran.nextInt(inputAgentDescriptions.size());
		return inputAgentDescriptions.get(indexAD);
	}
	
	public InputAgentDescriptions exportIntersection(InputAgentDescriptions inputAgentDscrs) {
		
		InputAgentDescriptions intersection = new InputAgentDescriptions();
		
		for (InputAgentDescription inputAgentDescriptionI : inputAgentDescriptions) {
			if (inputAgentDscrs.containsAgentDescription(
					inputAgentDescriptionI)) {
				intersection.addAgentDescriptions(inputAgentDescriptionI);
			}
		}
		
		return intersection;
	}
	
	/**
	 * Exports complement - {@link InputAgentDescription}s which are not in
	 * given structure
	 * @param descriptions
	 * @return
	 */
	public InputAgentDescriptions exportComplement(InputAgentDescriptions descriptions) {
		
		InputAgentDescriptions thisClone = this.deepClone();
		thisClone.removeAll(descriptions);
		
		return thisClone;
	}
	
	public List<MethodType> exportMethodTypes() {
		
		List<MethodType> methodTypes = new ArrayList<>();
		
		for (InputAgentDescription inputAgentDescriptionI :
			inputAgentDescriptions) {
			
			methodTypes.add(inputAgentDescriptionI.exportMethodType());
		}
		
		return methodTypes;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (inputAgentDescriptions == null) {
			return false;
		}
		for (InputAgentDescription agentDescriptionI : inputAgentDescriptions) {
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
		for (InputAgentDescription agentDescriptionI : inputAgentDescriptions) {
			descriptionsClone.add(
					agentDescriptionI.deepClone());
		}
		return new InputAgentDescriptions(descriptionsClone);
	}
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof InputAgentDescriptions)) {
	        return false;
	    }
	    
	    InputAgentDescriptions inputAgentDescriptionsOuther = (InputAgentDescriptions)other;
	    
	    if (inputAgentDescriptions.size() !=
	    		inputAgentDescriptionsOuther.size()) {
	    	return false;
	    }
	    for (int i = 0; i < inputAgentDescriptions.size(); i++) {
	    	
	    	InputAgentDescription iaI =
	    			inputAgentDescriptions.get(i);
	    	InputAgentDescription iadOutherI =
	    			inputAgentDescriptionsOuther.get(i);
	    	
	    	if (! iaI.equals(iadOutherI)) {
	    		return false;
	    	}
	    }
	    return true;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		String string = "";
		
		for (InputAgentDescription inputAgentDescrI : inputAgentDescriptions) {
			string += inputAgentDescrI.toString();
		}
		
		return string;
	}
	
}
