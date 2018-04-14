package org.distributedea.ontology.dataset.matrixfactorization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

/**
 * Structure represents one Raiting object
 * @author stepan
 *
 */
public class ObjectRaiting implements Concept {
	
	private static final long serialVersionUID = 1L;

	private int userID;
	private int itemID;
	private double raiting;
	
	@Deprecated
	public ObjectRaiting() {}

	/**
	 * Constructor
	 * @param numberOfObject
	 * @param size
	 */
	public ObjectRaiting(int userID, int itemID, double raiting) {
		setUserID(userID);
		setItemID(itemID);
		setRaiting(raiting);
	}

	/**
	 * Copy constructor
	 * @param objectBinPack
	 */
	public ObjectRaiting(ObjectRaiting objectRaiting) {
		if (objectRaiting == null || ! objectRaiting.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ObjectRaiting.class.getSimpleName() + " is not valid");
		}
		setUserID(objectRaiting.getUserID());
		setItemID(objectRaiting.getItemID());
		setRaiting(objectRaiting.getRaiting());
	}

	
	public int getUserID() {
		return userID;
	}
	
	@Deprecated
	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getItemID() {
		return itemID;
	}

	@Deprecated
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public double getRaiting() {
		return raiting;
	}

	@Deprecated
	public void setRaiting(double raiting) {
		this.raiting = raiting;
	}

	@Override
	public boolean equals(Object other) {
		
	    if (other == null) {
	    	throw new IllegalArgumentException();
	    }

	    if (!(other instanceof ObjectRaiting)) {
	        return false;
	    }
	    
	    ObjectRaiting oRaiting = (ObjectRaiting)other;
	    if (! oRaiting.valid(new TrashLogger())) {
	    	return false;
	    }
	    
	    return (this.getItemID() == oRaiting.getItemID()) &&
	    		(this.getUserID() == oRaiting.getUserID()) &&
	    		(this.getRaiting() == oRaiting.getRaiting());
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		return "" + this.userID + "-" + this.itemID + "-" + this.raiting;
	}
	
	
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return true;
	}
	
	/**
	 * Returns deep clone
	 * @return
	 */
	public ObjectRaiting deepClone() {
		return new ObjectRaiting(this);
	}
}
