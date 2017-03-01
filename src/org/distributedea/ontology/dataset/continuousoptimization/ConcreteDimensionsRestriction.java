package org.distributedea.ontology.dataset.continuousoptimization;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology represents restriction to dimension
 * @author stepan
 *
 */
public class ConcreteDimensionsRestriction extends DomainDefinition {

	private List<DimensionRestriction> restrictions;
	
	@Deprecated
	public ConcreteDimensionsRestriction() {
		this.restrictions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param interval
	 */
	public ConcreteDimensionsRestriction(List<DimensionRestriction> restrictions) {
		setRestrictions(restrictions);
	}

	/**
	 * Copy constructor
	 * @param restrictions
	 */
	public ConcreteDimensionsRestriction(ConcreteDimensionsRestriction restrictions) {
		if (restrictions == null ||
				! restrictions.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ConcreteDimensionsRestriction.class.getSimpleName() + " is not valid");
		}

		List<DimensionRestriction> restrictionsClone = new ArrayList<>();
		for (DimensionRestriction restI : restrictions.getRestrictions()) {
			restrictionsClone.add(
					restI.deepClone());
		}
		this.restrictions = restrictionsClone;
	}
	
	public List<DimensionRestriction> getRestrictions() {
		return restrictions;
	}
	public void setRestrictions(List<DimensionRestriction> restrictions) {
		if (restrictions == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (DimensionRestriction restI : restrictions) {
			if (restI == null || ! restI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						List.class.getSimpleName() + " is not valid");
			}
		}

		this.restrictions = restrictions;
	}
	
	@Override
	public Interval exportRestriction(int index) {
		if (getRestrictions() == null) {
			return null;
		}
		for (DimensionRestriction restI : getRestrictions()) {
			if (restI.getDimensionID() == index) {
				return restI.getRestriction();
			}
		}
		return null;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (getRestrictions() == null) {
			return false;
		}		
		for (DimensionRestriction restI : getRestrictions()) {
			if (restI == null || ! restI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public ConcreteDimensionsRestriction deepClone() {
		return new ConcreteDimensionsRestriction(this);
	}
	
}
