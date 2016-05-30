package org.distributedea.ontology.agentdescription;

import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.problems.ProblemTool;

import jade.content.Concept;

public class AgentDescription implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Agent configuration
	 */
	private AgentConfiguration agentConfiguration;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;


	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}
	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		this.agentConfiguration = agentConfiguration;
	}
	
	public String getProblemToolClass() {
		return problemToolClass;
	}
	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}

	public Class<?> exportProblemToolClass() {
		try {
			return Class.forName(problemToolClass);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public void importProblemTool(ProblemTool problemTool) {
		this.problemToolClass = problemTool.getClass().getName();
	}
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof AgentDescription)) {
	        return false;
	    }
	    
	    AgentDescription adOuther = (AgentDescription)other;
	    
	    boolean areAgentagentConfigurationsEqual =
	    		this.getAgentConfiguration().equals(adOuther.getAgentConfiguration());
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
		return agentConfiguration.toString() + problemToolClass;
	}
	
}
