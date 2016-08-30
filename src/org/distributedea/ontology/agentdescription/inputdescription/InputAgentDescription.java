package org.distributedea.ontology.agentdescription.inputdescription;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.problems.IProblemTool;


/**
 * Ontology represents requested description of method.
 * @author stepan
 *
 */
public class InputAgentDescription implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Agent specification including class, name and parameters
	 */
	private InputAgentConfiguration inputAgentConfiguration;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;

	
	
	@Deprecated
	public InputAgentDescription() {} // only for Jade
	
	/**
	 * Constructor
	 * @param agentConfiguration
	 * @param problemToolClass
	 */
	public InputAgentDescription(InputAgentConfiguration agentConfiguration,
			Class<?> problemToolClass) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
	
		this.inputAgentConfiguration = agentConfiguration;
		this.importProblemToolClass(problemToolClass);
	}

	public InputAgentConfiguration getInputAgentConfiguration() {
		return inputAgentConfiguration;
	}
	@Deprecated
	public void setInputAgentConfiguration(InputAgentConfiguration agentConfiguration) {
		if (agentConfiguration == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.inputAgentConfiguration = agentConfiguration;
	}
	
	public String getProblemToolClass() {
		return problemToolClass;
	}
	@Deprecated
	public void setProblemToolClass(String problemToolClass) {
		if (problemToolClass == null) {
			throw new IllegalArgumentException();
		}
		this.problemToolClass = problemToolClass;
	}

	/**
	 * Export Agent class
	 * @return
	 */
	public Class<?> exportAgentClass() {
		return inputAgentConfiguration.exportAgentClass();
	}
	
	/**
	 * Export {@link IProblemTool} class
	 * @return
	 */
	public Class<?> exportProblemToolClass() {
		try {
			return Class.forName(problemToolClass);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	/**
	 * Import {@link IProblemTool} class
	 * @param problemToolClass
	 */
	public void importProblemToolClass(Class<?> problemToolClass) {
		this.problemToolClass = problemToolClass.getName();
	}
	
	/**
	 * Export {@link MethodType}
	 * @return
	 */
	public MethodType exportMethodType() {
		
		Class<?> agentClass = getInputAgentConfiguration().exportAgentClass();
		Class<?> problemToolClass = exportProblemToolClass();
		
		return new MethodType(agentClass, problemToolClass);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (inputAgentConfiguration == null || ! inputAgentConfiguration.valid(logger)) {
			return false;
		}
		if (exportProblemToolClass() == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof AgentDescription)) {
	        return false;
	    }
	    
	    AgentDescription adOuther = (AgentDescription)other;
	    
	    boolean areAgentagentConfigurationsEqual =
	    		this.getInputAgentConfiguration().equals(adOuther.getAgentConfiguration());
	    boolean areProblemToolClassesEqual =
	    		this.getProblemToolClass().equals(adOuther.getProblemToolClass());
	    
	    if (areAgentagentConfigurationsEqual && 
	    		areProblemToolClassesEqual) {
	    	return true;
	    }
	    
	    return false;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		if (inputAgentConfiguration == null) {
			return "null" + problemToolClass;
		} else {
			return inputAgentConfiguration.toString() + problemToolClass;
		}
	}
	
	/**
	 * Exports clone
	 * @return
	 */
	public InputAgentDescription deepClone() {
		
		if (! valid(new TrashLogger())) {
			return null;
		}
		
		InputAgentConfiguration confClone = inputAgentConfiguration.deepClone();
		Class<?> problemToolClassClone = this.exportProblemToolClass();
		
		return new InputAgentDescription(confClone, problemToolClassClone);
	}
}