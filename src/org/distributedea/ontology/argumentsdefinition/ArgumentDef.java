package org.distributedea.ontology.argumentsdefinition;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;

import jade.content.Concept;

public abstract class ArgumentDef implements Concept {

	private static final long serialVersionUID = 1L;

	private String name;

	/**
	 * Constructor
	 */
	@Deprecated
	public ArgumentDef() { // Only for Jade
	}

	/**
	 * Constructor
	 * @param name
	 */
	public ArgumentDef(String name) {
		setName(name);
	}

	public abstract Argument exportRandomValue();
	public abstract Argument exportMinValue();
	public abstract Argument exportNeighbourValue(Argument current, double maxStep);
	public abstract Argument exportNextValue(Argument current, double maxStep);
	public abstract boolean exportIsTheLastValue(Argument argument);
	
	public abstract Argument exportCorrectedValue(Argument addend);
	public abstract Argument exportSumValue(Argument addend1, Argument addend2);
	public abstract Argument exportDifferenceValue(Argument minuend, Argument subtrahend);
	public abstract Argument exportProductValue(Argument factor1, double factor2);
	
	public abstract boolean representsRestrictionForFixedValue();
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Argument " +
					String.class.getSimpleName() + " is not valid");
		}
		this.name = name;
	}
	
	public abstract boolean valid(IAgentLogger logger);
	public abstract ArgumentDef deepClone();
}
