package org.distributedea.ontology.dataset.continuousoptimization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

public class AllDimensionsRestriction extends DomainDefinition {

	private Interval restriction;
	
	@Deprecated
	public AllDimensionsRestriction() {
	}
	
	/**
	 * Constructor
	 * @param interval
	 */
	public AllDimensionsRestriction(Interval interval) {
		setRestriction(interval);
	}

	/**
	 * Copy constructor
	 * @param restriction
	 */
	public AllDimensionsRestriction(AllDimensionsRestriction restriction) {
		if (restriction == null ||
				! restriction.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ConcreteDimensionsRestriction.class.getSimpleName() + " is not valid");
		}

		this.restriction = restriction.getRestriction().deepClone();
	}
	
	public Interval getRestriction() {
		return restriction;
	}
	public void setRestriction(Interval interval) {
		if (interval == null || ! interval.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Interval.class.getSimpleName() + " is not valid");
		}
		this.restriction = interval;
	}
	
	@Override
	public Interval exportRestriction(int index) {
		return getRestriction();
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (getRestriction() == null ||
				! getRestriction().valid(new TrashLogger())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public AllDimensionsRestriction deepClone() {
		return new AllDimensionsRestriction(this);
	}

}
