package org.distributedea.ontology.methoddescriptioncounter;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;

import jade.content.Concept;

/**
 * Ontology represents one Computing Agent helper and his priority(quality)
 * @author stepan
 *
 */
public class MethodDescriptionCounter implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodDescription description;
	private int counter;
	
	@Deprecated
	public MethodDescriptionCounter() {} //only for Jade

	/**
	 * Constructor
	 * @param description
	 * @param counter
	 */
	public MethodDescriptionCounter(MethodDescription description, int counter) {
		if (description == null || ! description.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.description = description;
		this.counter = counter;
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
	public int getCounter() {
		return counter;
	}
	@Deprecated
	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void increment() {
		this.counter++;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public MethodDescriptionCounter deepClone() {
		
		return new MethodDescriptionCounter(this.description.deepClone(), this.counter);
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
