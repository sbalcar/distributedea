package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;

/**
 * Ontology represents one latent factors based {@link Individual}
 * @author stepan
 *
 */
public class IndividualLatentFactors extends Individual {

	private static final long serialVersionUID = 1L;

	/** Latent factor X **/
	private LatentFactor latentFactorX;
	/** Latent factor Y **/
	private LatentFactor latentFactorY;
	
	
	@Deprecated
	public IndividualLatentFactors() {} // Only for JADE
	
	/**
	 * Constructor
	 * @param permutation
	 */
	public IndividualLatentFactors(LatentFactor latentFactorX,
			LatentFactor latentFactorY) {
		setLatentFactorX(latentFactorX);
		setLatentFactorY(latentFactorY);
	}
	
	/**
	 * Copy constructor
	 * @param individual
	 */
	public IndividualLatentFactors(IndividualLatentFactors individual) {
		if (individual == null || ! individual.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualLatentFactors.class.getSimpleName() +
					" is not valid");
		}
		setLatentFactorX(
				individual.getLatentFactorX().deepClone());
		setLatentFactorY(
				individual.getLatentFactorY().deepClone());
	}
	
	
	public LatentFactor getLatentFactorX() {
		return latentFactorX;
	}

	@Deprecated
	public void setLatentFactorX(LatentFactor latentFactorX) {
		if (latentFactorX == null || ! latentFactorX.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactor.class.getSimpleName() + " is not valid");
		}
		this.latentFactorX = latentFactorX;
	}

	public LatentFactor getLatentFactorY() {
		return latentFactorY;
	}

	@Deprecated
	public void setLatentFactorY(LatentFactor latentFactorY) {
		if (latentFactorY == null || ! latentFactorY.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactor.class.getSimpleName() + " is not valid");
		}
		this.latentFactorY = latentFactorY;
	}

	/**
	 * Export value counted from latent factors
	 * @param userIDIndex
	 * @param itemIDIndex
	 * @return
	 */
	public double exportValue(int userIDIndex, int itemIDIndex) {
		
		LatentFactorVector latFactVectorY =
				getLatentFactorY().exportLatentFactorVector(userIDIndex);
		LatentFactorVector latFactVectorX =
				getLatentFactorX().exportLatentFactorVector(itemIDIndex);
		
		return latFactVectorY.exportScalarProduct(latFactVectorX);
	}
	
	
	public List<Double> exportValues(int userIDIndex, List<Integer> itemIDIndexes) {
		
		List<Double> values = new ArrayList<>(); 
		for (int itemIDIndex : itemIDIndexes) {
			values.add(exportValue(userIDIndex, itemIDIndex));
		}
		
		return values;
	}
	
	@Override
	public boolean equals(Object other) {
	
	    if (!(other instanceof IndividualLatentFactors)) {
	        return false;
	    }

	    IndividualLatentFactors that = (IndividualLatentFactors) other;
	    
	    if (getLatentFactorX() == null && that.getLatentFactorX() != null) {
	    	return false;
	    }
	    
	    if (getLatentFactorY() == null && that.getLatentFactorY() != null) {
		    return false;
		}

	    if (getLatentFactorX().equals(that.getLatentFactorX()) &&
	    		getLatentFactorY().equals(that.getLatentFactorY()) ) {
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
		return latentFactorX.toString() + latentFactorY.toString();
	}
	
	
	@Override
	public String toLogString() {
		return toString();
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		
		if (latentFactorX == null || ! latentFactorX.valid(logger)) {
			return false;
		}

		if (latentFactorY == null || ! latentFactorY.valid(logger)) {
			return false;
		}
		return true;
	}

	@Override
	public Individual deepClone() {
		
		return new IndividualLatentFactors(this);
	}
	
}
