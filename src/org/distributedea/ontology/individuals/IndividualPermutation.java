package org.distributedea.ontology.individuals;

import java.util.List;

public class IndividualPermutation extends Individual {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> permutation = null;

	public List<Integer> getPermutation() {
		return permutation;
	}

	public void setPermutation(List<Integer> permutation) {
		this.permutation = permutation;
	}
	
}
