package org.distributedea.ontology.agentinfo;

import jade.content.Concept;

/**
 * Ontology represents description attitude towards to computation of
 * one Computing Agent
 * @author stepan
 *
 */
public class AgentInfo implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * computing agent class name
	 */
	private String computingAgentClassName;
	
	/**
	 * number of individuals
	 */
	private int numberOfIndividuals;
	
	/**
	 * search to local extremes
	 */
	private boolean exploitation;
	
	/**
	 * exploration of new solutions 
	 */
	private boolean exploration;
	
	@Deprecated
	public String getComputingAgentClassName() {
		return computingAgentClassName;
	}
	@Deprecated
	public void setComputingAgentClassName(String computingAgentClassName) {
		this.computingAgentClassName = computingAgentClassName;
	}
	public Class<?> exportComputingAgentClassName() {
		try {
			return Class.forName(getComputingAgentClassName());
		} catch (ClassNotFoundException e1) {
			return null;
		}
	}
	public void importComputingAgentClassName(Class<?> computingAgent) {
		if (computingAgent == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		computingAgentClassName = computingAgent.getName();
	}
	
	public int getNumberOfIndividuals() {
		return numberOfIndividuals;
	}
	public void setNumberOfIndividuals(int numberOfIndividuals) {
		this.numberOfIndividuals = numberOfIndividuals;
	}
	
	public boolean isExploitation() {
		return exploitation;
	}
	public void setExploitation(boolean exploitation) {
		this.exploitation = exploitation;
	}
	
	public boolean isExploration() {
		return exploration;
	}
	public void setExploration(boolean exploration) {
		this.exploration = exploration;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid() {
		if (exportComputingAgentClassName() == null) {
			return false;
		}
		if (numberOfIndividuals <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof AgentInfo)) {
	        return false;
	    }
	    
	    AgentInfo acOuther = (AgentInfo)other;
	    
	    boolean areNumberOfIndividualsEqual =
	    		this.getNumberOfIndividuals() == acOuther.getNumberOfIndividuals();
	    boolean areExploitationEqual =
	    		this.isExploitation() == acOuther.isExploitation();
	    boolean areExplorationEqual =
	    		this.isExploration() == acOuther.isExploration();
	    
	    if (areNumberOfIndividualsEqual && areExploitationEqual && areExplorationEqual) {
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
		
		return "" + getNumberOfIndividuals() +
				isExploitation() + isExploration();
	}
}
