package org.distributedea.ontology.methoddescriptioninput;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methodtype.MethodType;

/**
 * Ontology represents {@link List} of {@link InputMethodDescription} elements.
 * @author stepan
 *
 */
public class InputMethodDescriptions implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private List<InputMethodDescription> inputAgentDescriptions;

	/**
	 * Constructor
	 */
	public InputMethodDescriptions() {
		this.inputAgentDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentDescriptions
	 */
	public InputMethodDescriptions(List<InputMethodDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Constructor - Cartesian product of {@link InputAgentConfigurations}
	 * and {@link ProblemTools}
	 * @param configurations
	 * @param problemTools
	 */
	public InputMethodDescriptions(InputAgentConfigurations configurations,
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
		
		List<InputMethodDescription> descriptions = new ArrayList<>();
		
		for (InputAgentConfiguration configurationI : configurations.getAgentConfigurations()) {
			
			for (Class<?> problemToolsI : problemTools.getProblemTools()) {
				
				InputMethodDescription descriptionI =
						new InputMethodDescription(configurationI, problemToolsI);
				
				descriptions.add(descriptionI);
			}
		}
		
		importDescriptions(descriptions);
	}

	
	/**
	 * Copy constructor
	 * @param agentDescription
	 */
	public InputMethodDescriptions(InputMethodDescriptions agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescriptions.class.getSimpleName() + " is not valid");
		}
		InputMethodDescriptions agentDescriptionClone = agentDescription.deepClone();
		this.inputAgentDescriptions = agentDescriptionClone.getInputAgentDescriptions();
	}

	/**
	 * Returns list of {@link InputMethodDescription}.
	 * @return
	 */
	public List<InputMethodDescription> getInputAgentDescriptions() {
		return inputAgentDescriptions;
	}
	@Deprecated
	public void setInputAgentDescriptions(List<InputMethodDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Adds {@link InputMethodDescription}
	 * @param agentDescriptions
	 */
	public void addAgentDescriptions(InputMethodDescription agentDescriptions) {
		if (agentDescriptions == null ||
				! agentDescriptions.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescription.class.getSimpleName() + " is not valid");			
		}
		
		this.inputAgentDescriptions.add(agentDescriptions);
	}

	private void importDescriptions(List<InputMethodDescription> agentDescriptions) {
		if (agentDescriptions == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		IAgentLogger logger = new TrashLogger();
		for (InputMethodDescription agentDescriptionI : agentDescriptions) {
			if (agentDescriptionI == null ||
					! agentDescriptionI.valid(logger)) {
				agentDescriptionI.valid(logger);
				throw new IllegalArgumentException();
			}
		}
		this.inputAgentDescriptions = agentDescriptions;
	}
	
	/**
	 * Tests if this {@link MethodDescriptions} contains given {@link MethodDescription}.
	 * @param agentDescription
	 * @return
	 */
	public boolean containsAgentDescription(InputMethodDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					agentDescription.getClass().getSimpleName() + " is not valid.");
		}
		return inputAgentDescriptions.contains(agentDescription);
	}
	
	
	public InputMethodDescription get(int index) {
		
		return this.inputAgentDescriptions.get(index);
	}
	
	/**
	 * Removes {@link InputMethodDescription} in the specific position
	 * @param index
	 * @return
	 */
	public InputMethodDescription remove(int index) {
		
		if (index < 0) {
			throw new IllegalArgumentException("Argument " +
					int.class.getSimpleName() + " is not valid");
		}
		return this.inputAgentDescriptions.remove(index);
	}

	public boolean removeAll(InputMethodDescriptions inputAgentDscrs) {
		return this.inputAgentDescriptions.removeAll(
				inputAgentDscrs.getInputAgentDescriptions());
	}
	
	/**
	 * Returns true if this structure contains no {@link MethodDescription}
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
	 * Exports random selected {@link InputMethodDescription}
	 * @return
	 */
	public InputMethodDescription exportRandomInputAgentDescription() {
		
		if (inputAgentDescriptions.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int indexAD = ran.nextInt(inputAgentDescriptions.size());
		return inputAgentDescriptions.get(indexAD);
	}
	
	public InputMethodDescriptions exportIntersection(InputMethodDescriptions inputAgentDscrs) {
		
		InputMethodDescriptions intersection = new InputMethodDescriptions();
		
		for (InputMethodDescription inputAgentDescriptionI : inputAgentDescriptions) {
			if (inputAgentDscrs.containsAgentDescription(
					inputAgentDescriptionI)) {
				intersection.addAgentDescriptions(inputAgentDescriptionI);
			}
		}
		
		return intersection;
	}
	
	/**
	 * Exports complement - {@link InputMethodDescription}s which are not in
	 * given structure
	 * @param descriptions
	 * @return
	 */
	public InputMethodDescriptions exportComplement(InputMethodDescriptions descriptions) {
		
		InputMethodDescriptions thisClone = this.deepClone();
		thisClone.removeAll(descriptions);
		
		return thisClone;
	}
	
	public List<MethodType> exportMethodTypes() {
		
		List<MethodType> methodTypes = new ArrayList<>();
		
		for (InputMethodDescription inputAgentDescriptionI :
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
		for (InputMethodDescription agentDescriptionI : inputAgentDescriptions) {
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
	public InputMethodDescriptions deepClone() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		List<InputMethodDescription> descriptionsClone = new ArrayList<>();
		for (InputMethodDescription agentDescriptionI : inputAgentDescriptions) {
			descriptionsClone.add(
					agentDescriptionI.deepClone());
		}
		return new InputMethodDescriptions(descriptionsClone);
	}
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof InputMethodDescriptions)) {
	        return false;
	    }
	    
	    InputMethodDescriptions inputAgentDescriptionsOuther = (InputMethodDescriptions)other;
	    
	    if (inputAgentDescriptions.size() !=
	    		inputAgentDescriptionsOuther.size()) {
	    	return false;
	    }
	    for (int i = 0; i < inputAgentDescriptions.size(); i++) {
	    	
	    	InputMethodDescription iaI =
	    			inputAgentDescriptions.get(i);
	    	InputMethodDescription iadOutherI =
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
		
		for (InputMethodDescription inputAgentDescrI : inputAgentDescriptions) {
			string += inputAgentDescrI.toString();
		}
		
		return string;
	}
	
}
