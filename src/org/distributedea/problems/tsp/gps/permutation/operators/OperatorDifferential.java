package org.distributedea.problems.tsp.gps.permutation.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.distributedea.ontology.individuals.IndividualPermutation;

public class OperatorDifferential {
	
	//differential weight [0,2]
	static double F = 1;
			
	public static IndividualPermutation create(IndividualPermutation individualPerm1,
			IndividualPermutation individualPerm2, IndividualPermutation individualPerm3) {
		
		List<Integer> perm1 = individualPerm1.getPermutation();
		List<Integer> perm2 = individualPerm2.getPermutation();
		List<Integer> perm3 = individualPerm3.getPermutation();

		int min = Collections.min(perm1);
		
		List<ValueDif> values = new ArrayList<>();
		
		for (int i = 0; i < perm1.size(); i++) {
			
			double valueI = perm1.get(i) + F * (perm2.get(i) - perm3.get(i));
			values.add(new ValueDif(i+min, valueI));
		}
		
		Collections.sort(values, new CmpValueDif());
		
		List<Integer> newPerm = new ArrayList<>();
		
		for (ValueDif valueDifI : values) {
			newPerm.add(valueDifI.id);
		}
		
		return new IndividualPermutation(newPerm);
	}

}

class ValueDif {
	
	public ValueDif(int id, double value) {
		this.id = id;
		this.value = value;
	}
	
	int id;
	double value;
}

class CmpValueDif implements Comparator<ValueDif> {

	@Override
	public int compare(ValueDif arg0, ValueDif arg1) {
		
		double value0 = arg0.value;
		double value1 = arg1.value;
		
		if (value0 == value1) {
			return 0;
		}
		
		if (value0 < value1) {
			return -1;
		} else {
			return 1;
		}
	}
	
}