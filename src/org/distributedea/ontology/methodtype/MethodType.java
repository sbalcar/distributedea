package org.distributedea.ontology.methodtype;


import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;

import jade.content.Concept;


/**
 * Ontology represents one type of method
 * @author stepan
 *
 */
public class MethodType implements Concept {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Agent class
	 */
	private String agentClassName;

	/**
	 * Arguments of agent
	 */
	private Arguments arguments;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private ProblemToolDefinition problemToolDefinition;

	
	@Deprecated
	public MethodType() {} // only for Jade
	
	
	/**
	 * Constructor
	 * @param agentClass
	 * @param problemToolClass
	 */
	public MethodType(Class<?> agentClass, ProblemToolDefinition problemToolDefinition, Arguments arguments) {
		
		if (arguments == null || ! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
		
		importAgentClass(agentClass);
		setProblemToolDefinition(problemToolDefinition);
		this.arguments = arguments;
	}
	
	/**
	 * Copy Constructor
	 * @param methodType
	 */
	public MethodType(MethodType methodType) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			new IllegalArgumentException("Argument" +
					MethodType.class.getSimpleName() + " is not valid");
		}
		importAgentClass(methodType.exportAgentClass());
		setProblemToolDefinition(methodType.getProblemToolDefinition().deepClone());
		setArguments(methodType.getArguments().deepClone());
	}
	
	@Deprecated
	public String getAgentClassName() {
		return agentClassName;
	}
	@Deprecated
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	
	public Arguments getArguments() {
		return arguments;
	}
	@Deprecated
	public void setArguments(Arguments arguments) {
		if (arguments == null || ! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
		this.arguments = arguments;
	}


	
	public ProblemToolDefinition getProblemToolDefinition() {
		return problemToolDefinition;
	}
	@Deprecated
	public void setProblemToolDefinition(ProblemToolDefinition problemToolDefinition) {
		
		if (problemToolDefinition == null || ! problemToolDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolDefinition.class.getSimpleName() + " is not valid");
		}
		this.problemToolDefinition = problemToolDefinition;
	}


	/**
	 * Export Agent class
	 * @return
	 */
	public Class<?> exportAgentClass() {
		try {
			return Class.forName(agentClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	/**
	 * Import Agent class
	 * @param agentClass
	 */
	private void importAgentClass(Class<?> agentClass) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		this.agentClassName = agentClass.getName();
	}
	
	

	/**
	 * Exports {@link InputMethodDescription} of this {@link MethodType}
	 * @return
	 */
	public InputMethodDescription exportInputMethodDescription() {
	
		InputAgentConfiguration configuration = new InputAgentConfiguration(
				exportAgentClass().getSimpleName(), exportAgentClass(),
				getArguments());
		
		return new InputMethodDescription(configuration,
				getProblemToolDefinition().deepClone());
	}
	
	/**
	 * Test validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (exportAgentClass() == null) {
			return false;
		}
		if (getProblemToolDefinition() == null ||
				! getProblemToolDefinition().valid(logger)) {
			return false;
		}
		if (getArguments() == null || ! getArguments().valid(logger)) {
			return false;
		}
		return true;
	}

	/**
	 * Test same Method type
	 * @param other
	 * @return
	 */
	public boolean equalsMetodType(MethodType methodType) {
		
	    Class<?> agentClass = methodType.exportAgentClass();
	    Class<?> problemToolClass = 
	    		methodType.getProblemToolDefinition().exportProblemToolClass(new TrashLogger());
	    
		return exportAgentClass() == agentClass &&
				getProblemToolDefinition().exportProblemToolClass(new TrashLogger()) == problemToolClass;
	}
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodType)) {
	        return false;
	    }
	    
	    MethodType methodType = (MethodType)other;
	    
	    boolean areAgentClassEqual =
	    		exportAgentClass() == methodType.exportAgentClass();
	    boolean areProblemToolEqual =
	    		getProblemToolDefinition().equals(methodType.getProblemToolDefinition());
	    boolean areArgumentsEqual =
	    		getArguments().equals(methodType.getArguments());
	    
	    return areAgentClassEqual && areProblemToolEqual && areArgumentsEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return agentClassName + arguments.toString() +
				problemToolDefinition.toString();
	}

	/**
	 * Export type of method as String
	 * @param legendContainsProblemTools
	 * @param legendContainsArguments
	 * @return
	 */
	public String exportString(boolean legendContainsProblemTools,
			boolean legendContainsArguments) {
		
		String eString = exportAgentClass().getSimpleName();
				
		if (legendContainsArguments) {
			eString += "-" + arguments.toString();
		}

		if (legendContainsProblemTools) {
			eString += "-" + getProblemToolDefinition().toString();
		}
		
		return eString;
	}
	
	
	/**
	 * Exports clone
	 * @return
	 */
	public MethodType deepClone() {
		
		return new MethodType(this);
	}
}
