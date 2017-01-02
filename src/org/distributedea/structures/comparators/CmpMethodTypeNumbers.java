package org.distributedea.structures.comparators;

import java.util.Comparator;

import org.distributedea.ontology.methodtypenumber.MethodTypeNumber;

public class CmpMethodTypeNumbers implements Comparator<MethodTypeNumber> {

	@Override
	public int compare(MethodTypeNumber arg0,
			MethodTypeNumber arg1) {
		
		if (arg0.getNumber() < arg1.getNumber()) {
			return -1;
		} else if (arg0.getNumber() == arg1.getNumber()) {
			return 0;
		} else {
			return 1;
		}
	}

}