package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.List;

public class IndividualPermutation extends Individual {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> permutation = null;

	public List<Integer> getPermutation() {
		
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
		
		for (int i = 0; i < getPermutation().size(); i++) {
			int valueI = i +1;
			for (int itemI : getPermutation()) {
				if (valueI == itemI) {
					numberOfElement++;
				}
			}
		}
		
		if (numberOfElement == getPermutation().size()) {
			return true;
		} else {
			return false;
		}
	}
	
}
