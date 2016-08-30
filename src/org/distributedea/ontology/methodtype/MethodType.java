package org.distributedea.ontology.methodtype;


import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
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
	public MethodType(Class<?> agentClass, Class<?>  problemToolClass) {
		importAgentClass(agentClass);
		importProblemToolClass(problemToolClass);
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
	}
	
	@Deprecated
	public String getAgentClassName() {
		return agentClassName;
	}
	@Deprecated
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
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
		return true;
	}
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodType)) {
	        return false;
	    }
	    
	    MethodType methodType = (MethodType)other;
	    
	    return this.toString().equals(methodType.toString());
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return agentClassName + problemToolClassName;
	}

	public String exportString() {
		
		return exportAgentClass().getSimpleName() + "-" +
				exportProblemToolClass().getSimpleName();
	}
	
	
	/**
	 * Exports clone
	 * @return
	 */
	public MethodType exportClone() {
		
		return new MethodType(this);
	}
}
