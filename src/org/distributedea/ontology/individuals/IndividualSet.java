package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

public class IndividualSet extends Individual {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> set = null;

	@Deprecated
	public IndividualSet() {
		setSet(new ArrayList<Integer>());
	}

	/**
	 * Constructor
	 * @param set
	 */
	public IndividualSet(Set<Integer> set) {
		setSet(new ArrayList<>(set));
	}
	/**
	 * Constructor
	 * @param set
	 */
	public IndividualSet(List<Integer> set) {
		setSet(set);
	}
	
	/**
	 * Copy Constructor
	 * @param individual
	 */
	public IndividualSet(IndividualSet individual) {
		if (individual == null || ! individual.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualSet.class.getSimpleName() + " is not valid");
		}
		
		List<Integer> setClone = new ArrayList<>();
		for (Integer numI : individual.getSet()) {
			setClone.add(numI);
		}
		setSet(setClone);
	}
	
	public List<Integer> getSet() {
		return set;
	}
	@Deprecated
	public void setSet(List<Integer> set) {
		if (set == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		List<Integer> setCopy = new ArrayList<Integer>();

		for (Object itemI : set) {
			
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
			
			setCopy.add(value);
		}
		this.set = setCopy;
	}

	public Set<Integer> exportSet() {
		return new LinkedHashSet<>(set);
	}

	
	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public IndividualSet deepClone() {
		return new IndividualSet(this);
	}

	@Override
	public String toLogString() {
		return getSet().toString();
	}
	
}
