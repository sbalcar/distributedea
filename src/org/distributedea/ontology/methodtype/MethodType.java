package org.distributedea.ontology.methodtype;


import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.problems.IProblemTool;

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
	private String problemToolClassName;

	
	@Deprecated
	public MethodType() {} // only for Jade
	
	
	/**
	 * Constructor
	 * @param agentClass
	 * @param problemToolClass
	 */
	public MethodType(Class<?> agentClass, Class<?>  problemToolClass, Arguments arguments) {
		
		if (arguments == null || ! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
		
		importAgentClass(agentClass);
		importProblemToolClass(problemToolClass);
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
		importProblemToolClass(methodType.exportProblemToolClass());
		this.arguments = methodType.getArguments().deepClone();
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

	@Deprecated
	public String getProblemToolClassName() {
		return problemToolClassName;
	}
	@Deprecated
	public void setProblemToolClassName(String problemToolClass) {
		this.problemToolClassName = problemToolClass;
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
			throw new IllegalArgumentException();
		}
		this.agentClassName = agentClass.getName();
	}
	
	/**
	 * Exports {@link IProblemTool} class
	 * @return
	 */
	public Class<?> exportProblemToolClass() {
		try {
			return Class.forName(problemToolClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	/**
	 * Imports {@link IProblemTool} class
	 * @param problemToolClass
	 */
	private void importProblemToolClass(Class<?> problemToolClass) {
		if (problemToolClass == null) {
			throw new IllegalArgumentException();
		}
		this.problemToolClassName = problemToolClass.getName();
	}
	
	/**
	 * Exports {@link InputMethodDescription} of this {@link MethodType}
	 * @return
	 */
	public InputMethodDescription exportInputAgentDescription() {
	
		InputAgentConfiguration configuration = new InputAgentConfiguration(
				exportAgentClass().getSimpleName(), exportAgentClass(), getArguments());
		
		return new InputMethodDescription(configuration, exportProblemToolClass());
	}
	
	/**
	 * Test validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (exportAgentClass() == null) {
			return false;
		}
		if (exportProblemToolClass() == null) {
			return false;
		}
		if (getArguments() == null || ! getArguments().valid(logger)) {
			return false;
		}
		return true;
	}
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodType)) {
	        return false;
	    }
	    
	    MethodType methodType = (MethodType)other;
	    
	    boolean areAgentClassEqual =
	    		exportAgentClass() == methodType.exportAgentClass();
	    boolean areProblemToolEqual =
	    		exportProblemToolClass() == methodType.exportProblemToolClass();
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
		
		return agentClassName + problemToolClassName +
			arguments.toString();
	}

	public String exportString() {
		
		return exportAgentClass().getSimpleName() + "-" +
				exportProblemToolClass().getSimpleName() + "-" +
				arguments.toString();
	}
	
	
	/**
	 * Exports clone
	 * @return
	 */
	public MethodType exportClone() {
		
		return new MethodType(this);
	}
}
