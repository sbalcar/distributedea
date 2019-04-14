package org.distributedea.ontology.methoddesriptionsplanned;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

import jade.content.Concept;

/**
 * Ontology represents identification of method.
 * @author stepan
 *
 */
public class MethodIDs implements Concept {

	private static final long serialVersionUID = 1L;
	
	private int methodGlobalID;

	@Deprecated
	public MethodIDs() {} // only for Jade

	/**
	 * Constructor
	 * @param methodGlobalID
	 */
	public MethodIDs(int methodGlobalID) {
		setMethodGlobalID(methodGlobalID);
	}

	/**
	 * Copy constructor
	 * @param inputMethodDesc
	 */
	public MethodIDs(MethodIDs metodIDs) {
		if (metodIDs == null ||
				! metodIDs.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodIDs.class.getSimpleName() + " can not be null");
		}
		
		this.setMethodGlobalID(metodIDs.getMethodGlobalID());
	}
	
	public int getMethodGlobalID() {
		return methodGlobalID;
	}
	public void setMethodGlobalID(int methodGlobalID) {
		this.methodGlobalID = methodGlobalID;
	}

		
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodIDs)) {
	        return false;
	    }
	    
	    MethodIDs methodIDsOuther = (MethodIDs)other;
	    
	    boolean areMetodIDsEqual =
	    		this.getMethodGlobalID() == methodIDsOuther.getMethodGlobalID();
	    	    
	    return areMetodIDsEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return "" + getMethodGlobalID();
	}

	public String toLogString() {
		
		return "methodGlobalID=" + getMethodGlobalID();
	}

	/**
	 * Exports clone
	 * @return
	 */
	public MethodIDs deepClone() {
		
		return new MethodIDs(this);
	}
}
