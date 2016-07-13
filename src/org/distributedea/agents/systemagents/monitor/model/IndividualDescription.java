package org.distributedea.agents.systemagents.monitor.model;

import java.util.List;

public class IndividualDescription {

	private String individualHash;
	
	private double fitness;
	
	private List<Double> distances;

	
	public String getIndividualHash() {
		return individualHash;
	}
	public void setIndividualHash(String individualHash) {
		this.individualHash = individualHash;
	}

	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public List<Double> getDistances() {
		return distances;
	}
	public void setDistances(List<Double> distances) {
		this.distances = distances;
	} 
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof IndividualDescription)) {
	        return false;
	    }
	    
	    IndividualDescription idOuther = (IndividualDescription)other;
	    
	    boolean areIndividualHashEqual =
	    		this.getIndividualHash().equals(idOuther.getIndividualHash());
	    boolean areFitnessEqual =
	    		this.getFitness() == idOuther.getFitness();

	    if ((this.getDistances() == null) && (idOuther.getDistances() == null)) {
	    	return areIndividualHashEqual && areFitnessEqual;
	    }

	    if ( ((this.getDistances() == null) && (idOuther.getDistances() != null)) ||
	    		((this.getDistances() != null) && (idOuther.getDistances() == null)) ||
	    		(this.getDistances().size() != idOuther.getDistances().size()) ) {
	    	return false;
	    }
	    
	    boolean areDistancesEqual = true;
	    for (int indexI = 0; indexI < this.getDistances().size(); indexI++) {
	    	
	    	double distance1 = this.getDistances().get(indexI);
	    	double distance2 = idOuther.getDistances().get(indexI);
	    	
	    	if (distance1 != distance2) {
	    		areDistancesEqual = false;
	    	}
	    }
	    
	    return areIndividualHashEqual && areFitnessEqual && areDistancesEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
				
		String distances = "";
		if (getDistances() != null) {
		    for (double distanceI : getDistances()) {
		    	distances += distanceI;
		    }
		}	    
		return getIndividualHash() +
				getFitness() + distances;
	}
}
