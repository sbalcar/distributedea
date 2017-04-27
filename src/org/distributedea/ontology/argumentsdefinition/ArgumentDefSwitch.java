package org.distributedea.ontology.argumentsdefinition;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;

public class ArgumentDefSwitch extends ArgumentDef {

	private static final long serialVersionUID = 1L;

	@Deprecated
	public ArgumentDefSwitch() { // only for JADE
	}

	/**
	 * Constructor
	 * @param name
	 */
	public ArgumentDefSwitch(String name) {
		super(name);
	}

	/**
	 * Copy constructor
	 * @param argumentDef
	 */
	public ArgumentDefSwitch(ArgumentDefSwitch argumentDef) {
		super(argumentDef.getName());
	}
	
	@Override
	public Argument exportRandomValue() {
		boolean valueNew = Math.random() < 0.5;
		return new Argument(getName(), valueNew);
	}

	@Override
	public Argument exportMinValue() {
		return new Argument(getName(), true);
	}

	@Override
	public Argument exportNeighbourValue(Argument current, double maxStep) {
		
		boolean valueNew = current.exportValueAsBoolean();
		return new Argument(getName(), valueNew);
	}

	@Override
	public Argument exportNextValue(Argument current, double maxStep) {

		return exportNeighbourValue(current, maxStep);
	}

	@Override
	public boolean exportIsTheLastValue(Argument argument) {
		
		return ! argument.exportValueAsBoolean();
	}
	
	@Override
	public Argument exportCorrectedValue(Argument addend) {

		return addend.deepClone();
	}

	@Override
	public Argument exportSumValue(Argument addend1, Argument addend2) {

		boolean valueNew = addend1.exportValueAsBoolean();
		return new Argument(getName(), valueNew);
	}

	@Override
	public Argument exportDifferenceValue(Argument minuend, Argument subtrahend) {

		boolean valueNew = minuend.exportValueAsBoolean();
		return new Argument(getName(), valueNew);
	}

	@Override
	public Argument exportProductValue(Argument factor1, double factor2) {
		
		boolean valueNew = factor1.exportValueAsBoolean();
		return new Argument(getName(), valueNew);
	}

	@Override
	public boolean representsRestrictionForFixedValue() {

		return false;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		
		return true;
	}

	@Override
	public ArgumentDef deepClone() {
		
		return new ArgumentDefSwitch(this);
	}

}
