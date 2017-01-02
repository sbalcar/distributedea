package org.distributedea.ontology.methoddescriptionnumber;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;

import jade.content.Concept;

/**
 * Ontology represents one Computing Agent helper and his priority(quality)
 * @author stepan
 *
 */
public class MethodDescriptionNumber implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodDescription description;
	private int number;
	
	@Deprecated
	public MethodDescriptionNumber() {} //only for Jade

	/**
	 * Constructor
	 * @param description
	 * @param counter
	 */
	public MethodDescriptionNumber(MethodDescription description, int counter) {
		if (description == null || ! description.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		this.description = description;
		this.number = counter;
	}

	/**
	 * Copy Constructor
	 * @param methodDescriptionNumber
	 */
	public MethodDescriptionNumber(MethodDescriptionNumber methodDescriptionNumber) {
		if (methodDescriptionNumber == null || ! methodDescriptionNumber.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		this.description = methodDescriptionNumber.getDescription().deepClone();
		this.number = methodDescriptionNumber.getNumber();
	}
	
	/**
	 * Returns method
	 * @return
	 */
	public MethodDescription getDescription() {
		return description;
	}
	@Deprecated
	public void setDescription(MethodDescription gescription) {
		this.description = gescription;
	}
	
	/**
	 * Returns priority of method
	 * @return
	 */
	public int getNumber() {
		return number;
	}
	@Deprecated
	public void setNumber(int number) {
		this.number = number;
	}

	public void increment() {
		this.number++;
	}
	public void increment(int valueToIncrement) {
		this.number += valueToIncrement;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public MethodDescriptionNumber deepClone() {
		
		return new MethodDescriptionNumber(this);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (description == null || ! description.valid(logger)) {
			return false;
		}
		return true;
	}
}
