package org.distributedea.ontology.method;

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
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methodtype.MethodType;

/**
 * Ontology represents {@link List} of {@link InputMethodDescription} elements.
 * @author stepan
 *
 */
public class Methods implements IMethods, Concept {
	
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
	 * Constructor - Cartesian product of {@link InputAgentConfigurations}
	 * and {@link ProblemTools}
	 * @param configurations
	 * @param problemTools
	 */
	public Methods(InputAgentConfigurations configurations,
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
	 * @param methodDescriptions
	 * @param numberOfInstances
	 */
	public void addMethodDescriptions(InputMethodDescription methodDescriptions, int numberOfInstances) {
		if (numberOfInstances < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");			
		}
		
		for (int i = 0; i < numberOfInstances; i++) {
			addMethodDescriptions(methodDescriptions);
		}
	}
	
	/**
	 * Adds {@link InputMethodDescription}
	 * @param methodDescriptions
	 */
	public void addMethodDescriptions( List<InputMethodDescription> methodDescriptions) {
		if (methodDescriptions == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		
		this.inputMethodDescriptions.addAll(methodDescriptions);
	}
	
	/**
	 * Adds {@link InputMethodDescription}
	 * @param agentDescription
	 */
	public void addMethodDescriptions(InputMethodDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescription.class.getSimpleName() + " is not valid");			
		}
		
		this.inputMethodDescriptions.add(agentDescription);
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
				intersection.addMethodDescriptions(inputAgentDescriptionI);
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
	
	public List<MethodType> exportMethodTypes() {
		
		List<MethodType> methodTypes = new ArrayList<>();
		
		for (InputMethodDescription inputAgentDescriptionI :
			inputMethodDescriptions) {
			
			methodTypes.add(inputAgentDescriptionI.exportMethodType());
		}
		
		return methodTypes;
	}

	
	@Override
	public Methods exportInputMethodDescriptions() {
		return this;
	}

	@Override
	public InputAgentConfigurations exportInputAgentConfigurations() {
		
		List<InputAgentConfiguration> configurations = new ArrayList<>();
		
		for (InputMethodDescription methodI : getInputMethodDescriptions()) {
			InputAgentConfiguration confI =
					methodI.getInputAgentConfiguration();
			configurations.add(confI.deepClone());
		}
		
		return new InputAgentConfigurations(configurations);
	}
	@Override
	public ProblemTools exportProblemTools() {
		
		List<Class<?>> probTools = new ArrayList<>();
		
		for (InputMethodDescription methodI : getInputMethodDescriptions()) {
			
			Class<?> probToolI = methodI.exportProblemToolClass();
			probTools.add(probToolI);
		}
		
		return new ProblemTools(probTools);
	}

	@Override
	public InputMethodDescription exportRandomSelectedAgentDescription() {
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
