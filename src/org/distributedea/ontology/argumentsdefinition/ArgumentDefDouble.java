package org.distributedea.ontology.argumentsdefinition;

import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Argument;

public class ArgumentDefDouble extends ArgumentDef {

	private static final long serialVersionUID = 1L;
	
	private double min;
	private double max;

	/**
	 * Constructor
	 */
	@Deprecated
	public ArgumentDefDouble() { // Only for Jade
		super();
	}

	/**
	 * Constructor
	 * @param name
	 * @param min
	 * @param max
	 */
	public ArgumentDefDouble(String name, double min, double max) {
		super(name);
		setMin(min);
		setMax(max);
	}

	/**
	 * Copy constructor
	 * @param argumentDef
	 */
	public ArgumentDefDouble(ArgumentDefDouble argumentDef) {
		super("this text will be overwritten"); // temporary name of option
		if (argumentDef == null || ! argumentDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ArgumentDefInteger.class.getSimpleName() + " is not valid");
		}
		setName(argumentDef.getName());
		setMin(argumentDef.getMin());
		setMax(argumentDef.getMax());
	}

	
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
	
	public Argument exportRandomValue() {
		
		Random rnd = new Random();
		double randomValue = getMin() + (getMax() - getMin()) * rnd.nextDouble();
		
		return new Argument(getName(), randomValue);
	}

	public Argument exportMinValue() {
		return new Argument(getName(), getMin());
	}


	@Override
	public Argument exportCorrectedValue(Argument addend) {
		
		String name = addend.getName();
		Double value = addend.exportValueAsDouble();
		
		if (value < getMin()) {
			value = getMin();
		}
		if (value > getMax()) {
			value = getMax();
		}
		
		return new Argument(name, value);
	}
	
	@Override
	public Argument exportNeighbourValue(Argument current, double maxStep) {
		
		double value = current.exportValueAsDouble();
		
		Random rnd = new Random();
		
		double diff = rnd.nextDouble() * maxStep;
		if (rnd.nextBoolean()) {
			diff *= -1;
		}
		
		double valueNew = value + diff;
		return new Argument(current.getName(), valueNew);
	}

	@Override
	public Argument exportNextValue(Argument current, double maxStep) {
		
		double value = current.exportValueAsDouble();
		
		Random rnd = new Random();
		
		double diff = rnd.nextDouble() * maxStep;
		
		double valueNew = value + diff;
		return new Argument(current.getName(), valueNew);
	}
	
	@Override
	public boolean exportIsTheLastValue(Argument argument) {

		return getMax() <= argument.exportValueAsDouble();
	}
	
	@Override
	public Argument exportSumValue(Argument addend1, Argument addend2) {
		
		double addendInt1 = addend1.exportValueAsDouble();
		double addendInt2 = addend2.exportValueAsDouble();
		
		double sum = addendInt1 + addendInt2;
		
		return new Argument("Sum", sum);
	}
	
	@Override
	public Argument exportDifferenceValue(Argument minuend, Argument subtrahend) {
		
		double minuendDouble = minuend.exportValueAsDouble();
		double subtrahendDouble = subtrahend.exportValueAsDouble();
		
		double difference = minuendDouble -subtrahendDouble;
		
		return new Argument("Difference", difference);
	}
	
	@Override
	public Argument exportProductValue(Argument factor1, double factor2) {
		
		double value = factor1.exportValueAsDouble();
		
		double product = value * factor2;
		
		return new Argument("Product", product);
	}
	
	/**
	 * Represents Restriction for fixed value of {@link Argument}
	 */
	public boolean representsRestrictionForFixedValue() {
		
		return getMin() == getMax();
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		if (getMax() < getMin()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public ArgumentDef deepClone() {
		return new ArgumentDefDouble(this);
	}

}
