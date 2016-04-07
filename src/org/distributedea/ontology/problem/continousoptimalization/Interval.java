package org.distributedea.ontology.problem.continousoptimalization;

import jade.content.Concept;

public class Interval implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private double min;
	private double max;
	
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	
	public double size() {
		return getMax() - getMin();
	}
}
