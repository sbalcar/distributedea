package org.distributedea.ontology.dataset.continuousoptimization;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology defines restriction of one dimension
 * @author stepan
 *
 */
public class DimensionRestriction implements Concept {

	private static final long serialVersionUID = 1L;
	
	private int dimensionID;
	private Interval restriction;

	@Deprecated
	public DimensionRestriction() {
		this.dimensionID = -1;
	}

	/**
	 * Constructor
	 * @param dimensionID
	 * @param interval
	 */
	public DimensionRestriction(int dimensionID, Interval interval) {
		if (dimensionID < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");			
		}
		if (interval == null || ! interval.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Interval.class.getSimpleName() + " is not valid");
		}
		
		setDimensionID(dimensionID);
		setRestriction(interval);
	}
	
	/**
	 * Copy Constructor
	 * @param restriction
	 */
	public DimensionRestriction(DimensionRestriction restriction) {
		if (restriction == null || ! restriction.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Interval.class.getSimpleName() + " is not valid");
		}

		setDimensionID(restriction.getDimensionID());
		setRestriction(restriction.getRestriction().deepClone());
	}

	
	public int getDimensionID() {
		return dimensionID;
	}
	public void setDimensionID(int dimensionID) {
		this.dimensionID = dimensionID;
	}

	public Interval getRestriction() {
		return restriction;
	}
	public void setRestriction(Interval restriction) {
		if (restriction == null || ! restriction.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Interval.class.getSimpleName() + " is not valid");
		}
		this.restriction = restriction;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (getDimensionID() < 0) {
			return false;
		}
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
	public DimensionRestriction deepClone() {
		return new DimensionRestriction(this);
	}
}
