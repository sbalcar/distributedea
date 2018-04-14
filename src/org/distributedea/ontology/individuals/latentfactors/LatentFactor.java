package org.distributedea.ontology.individuals.latentfactors;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;

/**
 * Ontology represents one latent factor of {@link LatentFactorVector}
 * @author stepan
 *
 */
public class LatentFactor implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<LatentFactorVector> values;

	
	@Deprecated
	public LatentFactor() { // Only for JADE
		this.values = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param permutation
	 */
	public LatentFactor(List<LatentFactorVector> values) {
		setValues(values);
	}
	
	/**
	 * Copy constructor
	 * @param individual
	 */
	public LatentFactor(LatentFactor latentFactor) {
		if (latentFactor == null || ! latentFactor.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactor.class.getSimpleName() + " is not valid");
		}
		List<LatentFactorVector> valuesNew = new ArrayList<>();
		for (LatentFactorVector valueI : latentFactor.getValues()) {
			valuesNew.add(valueI.deepClone());
		}
		this.values = valuesNew;
	}

	
	
	public List<LatentFactorVector> getValues() {
		return values;
	}

	@Deprecated
	public void setValues(List<LatentFactorVector> values) {
		if (values == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (LatentFactorVector valueI : values) {
			if (valueI == null || ! valueI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						LatentFactorVector.class.getSimpleName() +
						" is not valid");
			}
		}
		this.values = values;
	}

	/**
	 * Returns size of the latent factor
	 * @return
	 */
	public int size() {
		if (values == null) {
			return 0;
		}
		
		return values.size();
	}

	public LatentFactorVector exportLatentFactorVector(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		if (getValues() == null || getValues().isEmpty()) {
			return null;
		}
		
		return getValues().get(index);
	}
	
	public void importLatentFactorVector(int index, LatentFactorVector vector) {
		if (index < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		if (vector == null || ! vector.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatentFactorVector.class.getSimpleName() + " is not valid");
		}
		
		this.values.set(index, vector);
	}
	
	@Override
	public boolean equals(Object other) {
	
	    if (!(other instanceof LatentFactor)) {
	        return false;
	    }

	    LatentFactor that = (LatentFactor) other;
	    
	    if (getValues() == null && that.getValues() != null) {
	    	return false;
	    }
	    
	    if (getValues().equals(that.getValues())) {
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
		return values.toString();
	}
	
	
	/**
	 * Validation
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (this.values == null) {
			return false;
		}
		for (LatentFactorVector valueI : values) {
			if (valueI == null || ! valueI.valid(logger)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns clone
	 * @return
	 */
	public LatentFactor deepClone() {
		
		return new LatentFactor(this);
	}

}

