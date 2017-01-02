package org.distributedea.structures.comparators;

import java.util.Comparator;

import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;

public class CpmMethodDescriptionNumbers implements Comparator<MethodDescriptionNumber> {

	@Override
	public int compare(MethodDescriptionNumber arg0,
			MethodDescriptionNumber arg1) {
		
		if (arg0.getNumber() < arg1.getNumber()) {
			return -1;
		} else if (arg0.getNumber() == arg1.getNumber()) {
			return 0;
		} else {
			return 1;
		}
	}

}
