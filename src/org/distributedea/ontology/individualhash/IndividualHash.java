package org.distributedea.ontology.individualhash;

import jade.content.Concept;

import java.util.List;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;

/**
 * Ontology represents compressed {@link Individual}
 * @author stepan
 *
 */
public class IndividualHash implements Concept {

	private static final long serialVersionUID = 1L;

	private String individualHash;
	
	private double fitness;
	
	private Pedigree pedigree;
	
	private List<Double> distances;

	/**
	 * Constructor
	 * @param indivEval
	 */
	public IndividualHash(IndividualEvaluated indivEval) {
		if (indivEval == null) {
			throw new IllegalArgumentException("Argument " +
					IndividualEvaluated.class.getSimpleName() + " is not valid");
		}
		setIndividualHash(indivEval.hashCode() + "");
		setFitness(indivEval.getFitness());
		setPedigree(indivEval.getPedigree());
		setDistances(null);
	}
	
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
	
	public Pedigree getPedigree() {
		return pedigree;
	}
	public void setPedigree(Pedigree pedigree) {
		this.pedigree = pedigree;
	}

	public List<Double> getDistances() {
		return distances;
	}
	public void setDistances(List<Double> distances) {
		this.distances = distances;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (other == null || !(other instanceof IndividualHash)) {
	        return false;
	    }
	    
	    IndividualHash idOuther = (IndividualHash)other;
	    
	    boolean areIndividualHashEqual =
	    		this.getIndividualHash().equals(idOuther.getIndividualHash());
	    boolean areFitnessEqual =
	    		this.getFitness() == idOuther.getFitness();
	    boolean arePedigreeEqual =
	    		(this.getPedigree() == null && idOuther.getPedigree() == null) ||
	    		(this.getPedigree() != null && this.getPedigree().equals(idOuther.getPedigree()));

	    
	    if ((this.getDistances() == null) && (idOuther.getDistances() == null)) {
	    	return areIndividualHashEqual && areFitnessEqual && arePedigreeEqual;
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
	    
	    return areIndividualHashEqual && areFitnessEqual &&
	    		arePedigreeEqual && areDistancesEqual;
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
