package org.distributedea.ontology.individuals.latentfactors;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

public class LatentFactorModel implements Concept {

	private static final long serialVersionUID = 1L;

	/** Latent factor X **/
	private LatentFactor latentFactorX;
	/** Latent factor Y **/
	private LatentFactor latentFactorY;
	
	
	@Deprecated
	public LatentFactorModel() {} // Only for JADE
	
	/**
	 * Constructor
	 * @param permutation
	 */
	public LatentFactorModel(LatentFactor latentFactorX,
			LatentFactor latentFactorY) {
		setLatentFactorX(latentFactorX);
		setLatentFactorY(latentFactorY);
	}
	
	/**
	 * Copy constructor
	 * @param latFaktModel
	 */
	public LatentFactorModel(LatentFactorModel latFaktModel) {
		if (latFaktModel == null || ! latFaktModel.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactorModel.class.getSimpleName() +
					" is not valid");
		}
		setLatentFactorX(
				latFaktModel.getLatentFactorX().deepClone());
		setLatentFactorY(
				latFaktModel.getLatentFactorY().deepClone());
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
	
	    if (!(other instanceof LatentFactorModel)) {
	        return false;
	    }

	    LatentFactorModel that = (LatentFactorModel) other;
	    
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
	
	
	public String toLogString() {
		return toString();
	}
	
	public boolean valid(IAgentLogger logger) {
		
		if (latentFactorX == null || ! latentFactorX.valid(logger)) {
			return false;
		}

		if (latentFactorY == null || ! latentFactorY.valid(logger)) {
			return false;
		}
		return true;
	}

	public LatentFactorModel deepClone() {
		
		return new LatentFactorModel(this);
	}

}
