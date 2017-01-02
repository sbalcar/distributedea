package org.distributedea.ontology.methodtypenumber;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methodtype.MethodType;

public class MethodTypeNumber implements Concept {

	private static final long serialVersionUID = 1L;
	
	private MethodType methodType;
	private int number;
	
	@Deprecated
	public MethodTypeNumber() {} //only for Jade
	
	/**
	 * Constructor
	 * @param methodType
	 * @param number
	 */
	public MethodTypeNumber(MethodType methodType, int number) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodType.class.getSimpleName() + " is not valid");
		}
		this.methodType = methodType;
		this.number = number;
	}
	
	/**
	 * Copy Constructor
	 * @param methodTypeNumber
	 */
	public MethodTypeNumber(MethodTypeNumber methodTypeNumber) {
		if (methodTypeNumber == null || ! methodTypeNumber.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodTypeNumber.class.getSimpleName() + " is not valid");
		}
		
		this.methodType = methodTypeNumber.getMethodType().deepClone();
		this.number = methodTypeNumber.getNumber();		
	}
	
	public MethodType getMethodType() {
		return methodType;
	}
	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public void incrementNumber() {
		this.number++;
	}
	public void incrementNumber(int number) {
		this.number += number;
	}	
	/**
	 * Returns clone
	 * @return
	 */
	public MethodTypeNumber deepClone() {
		
		return new MethodTypeNumber(this);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (methodType == null || ! methodType.valid(logger)) {
			return false;
		}
		return true;
	}
}
