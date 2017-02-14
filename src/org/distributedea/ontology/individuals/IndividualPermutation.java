package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology represents one permutation based {@link Individual}
 * @author stepan
 *
 */
public class IndividualPermutation extends Individual {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> permutation = null;

	
	@Deprecated
	public IndividualPermutation() {}
	
	/**
	 * Constructor
	 * @param permutation
	 */
	public IndividualPermutation(List<Integer> permutation) {
		setPermutation(permutation);
	}

	/**
	 * Copy constructor
	 * @param individual
	 */
	public IndividualPermutation(IndividualPermutation individual) {
		if (individual == null || ! individual.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		List<Integer> permutationNew = new ArrayList<>();
		for (Integer valueI : individual.getPermutation()) {
			permutationNew.add(new Integer(valueI));
		}
		permutation = permutationNew;
	}
	
	public List<Integer> getPermutation() {
		return permutation;
	}

	@Deprecated
	public void setPermutation(List<Integer> permutation) {
		if (permutation == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		List<Integer> permutationCopy = new ArrayList<Integer>();

		for (Object itemI : permutation) {
			
			Integer value = null;
			if (itemI instanceof Integer) {
				Integer integerValue = (Integer) itemI;
				value = integerValue;
			} else if (itemI instanceof Long) {
				Long longValue = (Long) itemI;
				long longV = longValue;
				int intV = (int)longV;
				value = intV;
			}
			
			permutationCopy.add(value);
		}

		this.permutation = permutationCopy;
	}
	
	public int sizeOfPermutation() {
		if (permutation == null) {
			return 0;
		}
		return permutation.size();
	}
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof IndividualPermutation)) {
	        return false;
	    }

	    IndividualPermutation that = (IndividualPermutation) other;

	    List<Integer> thisPermutation = this.getPermutation();
	    List<Integer> thatPermutation = that.getPermutation();
	    
	    // both permutations are null -> are the same 
	    if (thisPermutation == null && thatPermutation == null) {
	    	return true;
	    	
		// only one permutations is null -> aren't the same 
	    } else if (thisPermutation == null || thatPermutation == null) {
	    	return false;
	    }
	    
	    // check size of permutations
	    if (getPermutation().size() != that.getPermutation().size()) {
	        return false;
	    }
	    
	    // compare values of permutaions
	    for (int permutaionIndex = 0;
	    		permutaionIndex < getPermutation().size(); permutaionIndex++) {
	    	
	    	Integer value1 = thisPermutation.get(permutaionIndex);
	    	Integer value2 = thatPermutation.get(permutaionIndex);
	    	
	    	if (value1.intValue() != value2.intValue()) {
	    		return false;
	    	}

	    }
	    
	    return true;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		return permutation.toString();
	}
	
	
	@Override
	public String toLogString() {
		return permutation.toString();
	}
	
	/**
	 * Test validity
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (permutation == null) {
			return false;
		}
		
		int numberOfElement = 0;
		
		int minVal = Collections.min(getPermutation());
		
		for (int valueI = minVal; valueI <= getPermutation().size(); valueI++) {
			for (int itemI : getPermutation()) {
				if (valueI == itemI) {
					numberOfElement++;
				}
			}
		}
		
		if (numberOfElement == sizeOfPermutation()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns clone
	 */
	public Individual deepClone() {
		return new IndividualPermutation(this);
	}
	
}
