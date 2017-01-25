package org.distributedea.ontology.dataset.binpacking;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

/**
 * Structure represents one Bin Packing object
 * @author stepan
 *
 */
public class ObjectBinPack implements Concept {
	
	private static final long serialVersionUID = 1L;

	/** identification number of object **/
	private int numberOfObject;
	/** size of object **/
	private double size;
	
	@Deprecated
	public ObjectBinPack() {}
	
	/**
	 * Constructor
	 * @param numberOfObject
	 * @param size
	 */
	public ObjectBinPack(int numberOfObject, double size) {
		setNumberOfObject(numberOfObject);
		setSize(size);
	}

	/**
	 * Copy constructor
	 * @param objectBinPack
	 */
	public ObjectBinPack(ObjectBinPack objectBinPack) {
		if (objectBinPack == null || ! objectBinPack.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ObjectBinPack.class.getSimpleName() + " is not valid");
		}
		setNumberOfObject(objectBinPack.getNumberOfObject());
		setSize(objectBinPack.getSize());
	}
	
	public int getNumberOfObject() {
		return numberOfObject;
	}
	@Deprecated
	public void setNumberOfObject(int numberOfObject) {
		this.numberOfObject = numberOfObject;
	}
	
	public double getSize() {
		return size;
	}
	@Deprecated
	public void setSize(double size) {
		if (size <= 0 || 1 < size) {
			throw new IllegalArgumentException("Argument " +
					Double.class.getSimpleName() + " is not valid");
		}
		
		this.size = size;
	}
	
	/**
	 * Tests validity
	 * @param trashLogger
	 * @return
	 */
	public boolean valid(IAgentLogger trashLogger) {
		return true;
	}
	
	/**
	 * Returns deep clone
	 * @return
	 */
	public ObjectBinPack deepClone() {
		return new ObjectBinPack(this);
	}
	
}
