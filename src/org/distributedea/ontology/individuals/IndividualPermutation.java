package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndividualPermutation extends Individual {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> permutation = null;

	public List<Integer> getPermutation() {
		
		if (permutation == null) {
			return null;
		}
		
		List<Integer> permutationExport = new ArrayList<Integer>();

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
			
			permutationExport.add(value);
		}

		return permutationExport;
	}

	public void setPermutation(List<Integer> permutation) {
		this.permutation = permutation;
	}
	
	public int sizeOfPermutation() {
		return permutation.size();
	}
	
	public boolean validation() {
		
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
	    	
	    	Object value1 = permutation.get(permutaionIndex);
	    	Object value2 = that.getPermutation().get(permutaionIndex);
	    	
	    	if (value1 != value2) {
	    		return false;
	    	}

	    }
	    
	    return true;
	}

	@Override
	public String toLogString() {
		return permutation.toString();
	}
	
}
