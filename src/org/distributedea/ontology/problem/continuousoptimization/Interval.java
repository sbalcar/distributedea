package org.distributedea.ontology.problem.continuousoptimization;

import jade.content.Concept;

/**
 * Ontology for interval of continuous optimization
 * @author stepan
 *
 */
public class Interval implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private double min;
	private double max;
	
	@Deprecated
	public Interval() {} // only for Jade

	/**
	 * Constructor
	 * @param interval
	 */
	public Interval(double min, double max) {
		setMin(min);
		setMax(max);
	}

	/**
	 * Copy constructor
	 * @param interval
	 */
	public Interval(Interval interval) {
		if (interval == null || ! interval.valid()) {
			throw new IllegalArgumentException("Argument " +
					Interval.class.getSimpleName() + " is not valid");
		}
		setMin(interval.getMin());
		setMax(interval.getMax());
	}
	
	public double getMin() {
		return min;
	}
	@Deprecated
	public void setMin(double min) {
		this.min = min;
	}
	
	public double getMax() {
		return max;
	}
	@Deprecated
	public void setMax(double max) {
		this.max = max;
	}
	
	public double size() {
		return getMax() - getMin();
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid() {
		return min < max;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public Interval deepClone() {
		return new Interval(this);
	}
}
