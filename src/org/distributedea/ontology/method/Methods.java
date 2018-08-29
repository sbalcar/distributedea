package org.distributedea.ontology.method;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemtooldefinition.ProblemToolsDefinition;

/**
 * Ontology represents {@link List} of {@link InputMethodDescription} elements.
 * @author stepan
 *
 */
public class Methods implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private List<InputMethodDescription> inputMethodDescriptions;

	/**
	 * Constructor
	 */
	public Methods() {
		this.inputMethodDescriptions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentDescriptions
	 */
	public Methods(List<InputMethodDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Constructor
	 * @param agentDescriptions
	 */
	public Methods(InputMethodDescription agentDescription) {
		importDescriptions(Arrays.asList(agentDescription));
	}

	
	/**
	 * Constructor - Cartesian product of {@link InputAgentConfigurations}
	 * and {@link ProblemToolsDefinition}
	 * @param configurations
	 * @param problemTools
	 */
	public Methods(InputAgentConfigurations configurations,
			ProblemToolsDefinition problemTools) {
		
		if (configurations == null ||
				! configurations.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfigurations.class.getSimpleName() + "is not valid");
		}
		if (problemTools == null ||
				! problemTools.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolsDefinition.class.getSimpleName() + "is not valid");
		}
		
		List<InputMethodDescription> descriptions = new ArrayList<>();
		
		for (InputAgentConfiguration configurationI : configurations.getAgentConfigurations()) {
			
			for (ProblemToolDefinition problemToolsI : problemTools.getProblemToolsDefinition()) {
				
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
	public Methods(Methods agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Methods.class.getSimpleName() + " is not valid");
		}
		Methods agentDescriptionClone = agentDescription.deepClone();
		this.inputMethodDescriptions = agentDescriptionClone.getInputMethodDescriptions();
	}

	/**
	 * Returns list of {@link InputMethodDescription}.
	 * @return
	 */
	public List<InputMethodDescription> getInputMethodDescriptions() {
		return inputMethodDescriptions;
	}
	@Deprecated
	public void setInputMethodDescriptions(List<InputMethodDescription> agentDescriptions) {
		importDescriptions(agentDescriptions);
	}

	/**
	 * Adds {@link InputMethodDescription} multiple times
	 * @param inputMethodDescription
	 * @param numberOfInstances
	 */
	public void addInputMethodDescriptions(InputMethodDescription inputMethodDescription, int numberOfInstances) {
		if (numberOfInstances < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");			
		}
		
		for (int i = 0; i < numberOfInstances; i++) {
			addInputMethodDescr(inputMethodDescription);
		}
	}
	
	/**
	 * Adds {@link InputMethodDescription}
	 * @param inputMethodDescriptions
	 */
	public void addInputMethodDescriptions( List<InputMethodDescription> inputMethodDescriptions) {
		if (inputMethodDescriptions == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		
		this.inputMethodDescriptions.addAll(inputMethodDescriptions);
	}
	
	/**
	 * Adds {@link InputMethodDescription}
	 * @param inputMethodDescr
	 */
	public void addInputMethodDescr(InputMethodDescription inputMethodDescr) {
		if (inputMethodDescr == null ||
				! inputMethodDescr.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescription.class.getSimpleName() + " is not valid");			
		}
		
		this.inputMethodDescriptions.add(inputMethodDescr);
	}

	public void addInputMethodDescrCartesianProduct(InputAgentConfigurations inputAgentConf,
			ProblemToolsDefinition problemTools) {
		if (inputAgentConf == null ||
				! inputAgentConf.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfigurations.class.getSimpleName() + " is not valid");			
		}
		if (problemTools == null ||
				! problemTools.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolsDefinition.class.getSimpleName() + " is not valid");			
		}
		
		for (InputAgentConfiguration inputAgentConfI :
			inputAgentConf.getAgentConfigurations()) {
			
			for (ProblemToolDefinition problemToolI : problemTools.getProblemToolsDefinition()) {
				
				InputMethodDescription descriptionI =
						new InputMethodDescription(inputAgentConfI, problemToolI);
				
				addInputMethodDescr(descriptionI);
			}			
		}
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
		this.inputMethodDescriptions = agentDescriptions;
	}
	
	/**
	 * Tests if this {@link MethodDescriptions} contains given {@link MethodDescription}.
	 * @param methodDescription
	 * @return
	 */
	public boolean containsMethodDescription(InputMethodDescription methodDescription) {
		if (methodDescription == null ||
				! methodDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					methodDescription.getClass().getSimpleName() + " is not valid.");
		}
		return inputMethodDescriptions.contains(methodDescription);
	}
	
	
	public InputMethodDescription get(int index) {
		
		return this.inputMethodDescriptions.get(index);
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
		return this.inputMethodDescriptions.remove(index);
	}

	public boolean removeAll(Methods inputAgentDscrs) {
		return this.inputMethodDescriptions.removeAll(
				inputAgentDscrs.getInputMethodDescriptions());
	}
	
	/**
	 * Returns true if this structure contains no {@link MethodDescription}
	 * @return
	 */
	public boolean isEmpty() {

		return this.inputMethodDescriptions.isEmpty();
	}

	/**
	 * Returns size
	 * @return
	 */
	public int size() {

		return this.inputMethodDescriptions.size();
	}
	
	public Methods exportIntersection(Methods methodAgentDescrs) {
		
		Methods intersection = new Methods();
		
		for (InputMethodDescription inputAgentDescriptionI : inputMethodDescriptions) {
			if (methodAgentDescrs.containsMethodDescription(
					inputAgentDescriptionI)) {
				intersection.addInputMethodDescr(inputAgentDescriptionI);
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
	public Methods exportComplement(Methods descriptions) {
		
		Methods thisClone = this.deepClone();
		thisClone.removeAll(descriptions);
		
		return thisClone;
	}
	
	public InputMethodDescription exportFirstInputMethodDescription(Class<?> agentClass) {
		
		for (InputMethodDescription inputMethDescrI : this.inputMethodDescriptions) {
			
			InputAgentConfiguration inputAgentConfI = inputMethDescrI.getInputAgentConfiguration();
			if (inputAgentConfI.exportAgentClass() == agentClass) {
				return inputMethDescrI.deepClone();
			}
		}
		return null;
	}
	
	public List<MethodType> exportMethodTypes() {
		
		List<MethodType> methodTypes = new ArrayList<>();
		
		for (InputMethodDescription inputAgentDescriptionI :
			inputMethodDescriptions) {
			
			methodTypes.add(inputAgentDescriptionI.exportMethodType());
		}
		
		return methodTypes;
	}

	
	public Methods exportInputMethodDescriptions() {
		return this;
	}

	public InputAgentConfigurations exportInputAgentConfigurations() {
		
		List<InputAgentConfiguration> configurations = new ArrayList<>();
		
		for (InputMethodDescription methodI : getInputMethodDescriptions()) {
			InputAgentConfiguration confI =
					methodI.getInputAgentConfiguration();
			configurations.add(confI.deepClone());
		}
		
		return new InputAgentConfigurations(configurations);
	}
	
	public ProblemToolsDefinition exportProblemTools() {
		
		List<ProblemToolDefinition> probTools = new ArrayList<>();
		
		for (InputMethodDescription methodI : getInputMethodDescriptions()) {
			
			ProblemToolDefinition probToolI = methodI.getProblemToolDefinition();
			probTools.add(probToolI);
		}
		
		return new ProblemToolsDefinition(probTools);
	}

	public InputMethodDescription exportRandomMethodDescription() {
		if (inputMethodDescriptions.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int indexAD = ran.nextInt(inputMethodDescriptions.size());
		return inputMethodDescriptions.get(indexAD);
	}
	
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (inputMethodDescriptions == null) {
			return false;
		}
		for (InputMethodDescription agentDescriptionI : inputMethodDescriptions) {
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
	public Methods deepClone() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		List<InputMethodDescription> descriptionsClone = new ArrayList<>();
		for (InputMethodDescription agentDescriptionI : inputMethodDescriptions) {
			descriptionsClone.add(
					agentDescriptionI.deepClone());
		}
		return new Methods(descriptionsClone);
	}
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof Methods)) {
	        return false;
	    }
	    
	    Methods inputAgentDescriptionsOuther = (Methods)other;
	    
	    if (inputMethodDescriptions.size() !=
	    		inputAgentDescriptionsOuther.size()) {
	    	return false;
	    }
	    for (int i = 0; i < inputMethodDescriptions.size(); i++) {
	    	
	    	InputMethodDescription iaI =
	    			inputMethodDescriptions.get(i);
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
		
		for (InputMethodDescription inputAgentDescrI : inputMethodDescriptions) {
			string += inputAgentDescrI.toString();
		}
		
		return string;
	}

	
}
