package org.distributedea.ontology.argumentsdefinition;

import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Argument;


public class ArgumentDefInteger extends ArgumentDef {

	private static final long serialVersionUID = 1L;
	
	private int min;
	private int max;

	/**
	 * Constructor
	 */
	@Deprecated
	public ArgumentDefInteger() { // Only for Jade
		super();
	}

	/**
	 * Constructor
	 * @param name
	 * @param min
	 * @param max
	 */
	public ArgumentDefInteger(String name, int min, int max) {
		super(name);
		setMin(min);
		setMax(max);
	}

	/**
	 * Copy constructor
	 * @param argumentDef
	 */
	public ArgumentDefInteger(ArgumentDefInteger argumentDef) {
		super("this text will be overwritten"); // temporary name of option
		if (argumentDef == null || ! argumentDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ArgumentDefInteger.class.getSimpleName() + " is not valid");
		}
		setName(argumentDef.getName());
		setMin(argumentDef.getMin());
		setMax(argumentDef.getMax());
	}

	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}

	public Argument exportRandomValue() {
		
		Random rnd = new Random();
		int randomValue = getMin() + rnd.nextInt(getMax() +1 - getMin());
		
		return new Argument(getName(), randomValue);
	}
	
	public Argument exportMinValue() {
		return new Argument(getName(), getMin());
	}
	
	@Override
	public Argument exportCorrectedValue(Argument addend) {
		
		String name = addend.getName();
		int value = addend.exportValueAsInteger();
		
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
				
		int value = current.exportValueAsInteger();
		
		Random rnd = new Random();
		
		int diff = 1; 
		if (rnd.nextBoolean()) {
			diff *= -1;
		}

		int valueNew = value + diff;
				
		return new Argument(current.getName(), valueNew);
	}

	@Override
	public Argument exportNextValue(Argument current, double maxStep) {
		int maxStepInt = (int) maxStep;
		if (maxStepInt == 0) {
			maxStepInt = 1;
		}
		
		int value = current.exportValueAsInteger();
		
		int valueNew = value + 1;
		return new Argument(current.getName(), valueNew);
	}
	
	@Override
	public boolean exportIsTheLastValue(Argument argument) {

		return getMax() <= argument.exportValueAsInteger();
	}

	
	@Override
	public Argument exportSumValue(Argument addend1, Argument addend2) {

		int addendInt1 = addend1.exportValueAsInteger();
		int addendInt2 = addend2.exportValueAsInteger();
		
		int sum = addendInt1 + addendInt2;
		
		return new Argument("Sum", sum);
	}
	
	@Override
	public Argument exportDifferenceValue(Argument minuend, Argument subtrahend) {
		
		int minuendInt = minuend.exportValueAsInteger();
		int subtrahendInt = subtrahend.exportValueAsInteger();
		
		int difference = minuendInt -subtrahendInt;
		
		return new Argument("Difference", difference + "");
	}
	
	@Override
	public Argument exportProductValue(Argument factor1, double factor2) {
		
		int value = factor1.exportValueAsInteger();
		
		int product = (int) (value * factor2);
		
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
		return new ArgumentDefInteger(this);
	}
}
