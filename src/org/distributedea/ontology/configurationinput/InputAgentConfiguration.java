package org.distributedea.ontology.configurationinput;

import jade.content.Concept;

import java.util.ArrayList;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;

/**
 * Ontology represents requested configuration for the new agent
 * @author stepan
 *
 */
public class InputAgentConfiguration implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private String agentName;
	private String agentClassName;
	private Arguments arguments;

	
	@Deprecated
	public InputAgentConfiguration() {   // only for Jade
		this.arguments = new Arguments(new ArrayList<Argument>());
	}
	
	/**
	 * Constructor
	 * @param agentName
	 * @param agentClass
	 * @param arguments
	 */
	public InputAgentConfiguration(String agentName, Class<?> agentClass,
			Arguments arguments) {
		if (agentName == null || agentName.isEmpty()) {
			throw new IllegalArgumentException("Arguments " +
					String.class.getSimpleName() + " is not valid");			
		}
		if (arguments == null ||
				! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Arguments " +
				Arguments.class.getSimpleName() + " is not valid");
		}
		
		this.agentName = agentName;
		this.importAgentClass(agentClass);
		this.arguments = arguments;
	}
	
	/**
	 * Copy constructor
	 * @param inputAgentConfiguration
	 */
	public InputAgentConfiguration(InputAgentConfiguration
			inputAgentConfiguration) {
		if (inputAgentConfiguration == null ||
				! inputAgentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
		
		this.agentName = inputAgentConfiguration.getAgentName();
		this.importAgentClass(inputAgentConfiguration.exportAgentClass());
		this.arguments =
				inputAgentConfiguration.getArguments().deepClone();
	}
	
	/**
	 * Returns name of agent
	 * @return
	 */
	public String getAgentName() {
		return agentName;
	}
	@Deprecated
	public void setAgentName(String agentName) {
		if (agentName == null || agentName.isEmpty() ||
				agentName.contains(" ")) {
			throw new IllegalArgumentException("Argument " +
					String.class.getSimpleName() + " is not valid");
		}
		this.agentName = agentName;
	}
	@Deprecated
	public String getAgentClassName() {
		return agentClassName;
	}
	@Deprecated
	public void setAgentClassName(String agentType) {
		this.agentClassName = agentType;
	}
	
	/**
	 * Exports {@link Class} of agent
	 * @return
	 */
	public Class<?> exportAgentClass() {
		try {
			return Class.forName(getAgentClassName());
		} catch (ClassNotFoundException e1) {
			return null;
		}
	}
	/**
	 * Imports {@link Class} of agent
	 * @param agentClass
	 */
	public void importAgentClass(Class<?> agentClass) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " can not be null");
		}
		this.agentClassName = agentClass.getName();
	}
	
	/**
	 * Returns {@link Arguments}
	 * @return
	 */
	public Arguments getArguments() {
		return arguments;
	}
	@Deprecated
	public void setArguments(Arguments arguments) {
		if (arguments == null ||
				! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.arguments = arguments;
	}
	
	public boolean exportIsComputingAgent() {
		
		String namespace = Agent_BruteForce.class.getPackage().getName();
		if (agentClassName.startsWith(namespace)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (agentName == null) {
			return false;
		}
		Class<?> agentClass = exportAgentClass();
		if (agentClass == null) {
			return false;
		}
		if (arguments == null || ! arguments.valid(logger)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
	public InputAgentConfiguration deepClone() {
		return new InputAgentConfiguration(this);
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof InputAgentConfiguration)) {
	        return false;
	    }
	    
	    InputAgentConfiguration acOuther = (InputAgentConfiguration)other;
	    
	    boolean aregetAgentNamesEqual =
	    		this.getAgentName().equals(acOuther.getAgentName());
	    boolean areAgentTypeEqual =
	    		this.exportAgentClass() == acOuther.exportAgentClass();
	    boolean areArgumentsEqual =
	    		this.getArguments().equals(acOuther.getArguments());
	    
	    return aregetAgentNamesEqual && areAgentTypeEqual && areArgumentsEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return agentName + agentClassName + arguments.toString();
	}
}