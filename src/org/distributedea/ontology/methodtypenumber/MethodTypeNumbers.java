package org.distributedea.ontology.methodtypenumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methodtype.MethodType;
import org.distributedea.structures.comparators.CmpMethodTypeNumbers;

import jade.content.Concept;

public class MethodTypeNumbers implements Concept {

	private static final long serialVersionUID = 1L;

	private List<MethodTypeNumber> numbers;

	/**
	 * Constructor
	 */
	public MethodTypeNumbers() {
		this.numbers = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param numbers
	 */
	public MethodTypeNumbers(List<MethodTypeNumber> numbers) {
		if (numbers == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (MethodTypeNumber numberI : numbers) {
			if (numberI == null || ! numberI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						MethodTypeNumber.class.getSimpleName() + " is not valid");				
			}
		}
		this.numbers = numbers;
	}

	/**
	 * Copy constructor
	 * @param methodTypeNumbers
	 */
	public MethodTypeNumbers(MethodTypeNumbers methodTypeNumbers) {
		this(methodTypeNumbers.getNumbers());
	}
	
	
	public List<MethodTypeNumber> getNumbers() {
		return numbers;
	}
	public void setNumbers(List<MethodTypeNumber> numbers) {
		this.numbers = numbers;
	}

	public void addAndIncrementNumber(MethodType methodType) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodTypeNumber.class.getSimpleName() + " is not valid");
		}
		
		MethodTypeNumber methodTypeNumber =
				exportMethodTypeNumberOf(methodType);
		if (methodTypeNumber == null) {
			this.numbers.add(new MethodTypeNumber(methodType, 1));
		} else {
			methodTypeNumber.incrementNumber();
		}
	}
	
	public void addAndIncrementNumber(MethodTypeNumber methodTypeNumber) {
		if (methodTypeNumber == null || ! methodTypeNumber.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodTypeNumber.class.getSimpleName() + " is not valid");
		}

		MethodTypeNumber exportedMethodTypeNumber =
				exportMethodTypeNumberOf(methodTypeNumber.getMethodType());
		if (exportedMethodTypeNumber == null) {
			this.numbers.add(methodTypeNumber);
		} else {
			exportedMethodTypeNumber.incrementNumber(
					methodTypeNumber.getNumber());
		}
		

	}
	
	public MethodTypeNumber exportMethodTypeNumberOf(MethodType methodType) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodType.class.getSimpleName() + " is not valid");
		}
		for (MethodTypeNumber numberI : this.numbers) {
			MethodType methodTypeI = numberI.getMethodType();
			if (methodTypeI.equals(methodType)) {
				return numberI;
			}
		}
		return null;
	}

	
	public MethodTypeNumber exportMethodTypeNumberWithMaxNumber() {
		
		return Collections.max(this.numbers, new CmpMethodTypeNumbers());	
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public MethodTypeNumbers deepClone() {
		
		return new MethodTypeNumbers(this);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (numbers == null) {
			return false;
		}
		for (MethodTypeNumber numberI : numbers) {
			if (numberI == null || ! numberI.valid(new TrashLogger())) {
				return false;
			}
		}
		return true;
	}
}
