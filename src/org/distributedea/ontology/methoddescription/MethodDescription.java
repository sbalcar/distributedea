package org.distributedea.ontology.methoddescription;

import jade.content.Concept;

public class MethodDescription implements Concept {

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
	
	
	public String getComputingAgentClassName() {
		return computingAgentClassName;
	}
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
			computingAgentClassName = null;
		} else {
			computingAgentClassName = computingAgent.getName();
		}
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
	
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodDescription)) {
	        return false;
	    }
	    
	    MethodDescription acOuther = (MethodDescription)other;
	    
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
