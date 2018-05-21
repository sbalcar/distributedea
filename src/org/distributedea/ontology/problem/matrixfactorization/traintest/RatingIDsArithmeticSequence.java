package org.distributedea.ontology.problem.matrixfactorization.traintest;

import org.distributedea.logging.IAgentLogger;

/**
 * Ontology represents the sequence of RatingID
 * @author stepan
 *
 */
public class RatingIDsArithmeticSequence implements IRatingIDs {

	private static final long serialVersionUID = 1L;
	
	private int a1;
	private int d;
	
	@Deprecated
	public RatingIDsArithmeticSequence() {  // Only for Jade
	}
	
	/**
	 * Constructor
	 * @param a1
	 * @param d
	 */
	public RatingIDsArithmeticSequence(int a1, int d) {
		this.setA1(a1);
		this.setD(d);
	}

	public int getA1() {
		return a1;
	}
	public void setA1(int a1) {
		if (a1 < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		this.a1 = a1;
	}

	public int getD() {
		return d;
	}
	public void setD(int d) {
		if (d < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		this.d = d;
	}
	
	/**
	 * Contains rating ID 
	 * @param ratingID
	 * @return
	 */
	@Override
	public boolean containsRatingID(int ratingID) {
		if (ratingID < 1) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		if (ratingID < getA1()) {
			return false;
		}
		return ((ratingID - getA1()) % getD()) == 0;
	}
	
	/**
	 * Export Ai of current arithmetic sequence
	 * @param i
	 * @return
	 */
	public int exportAi(int i) {
		if (i <= 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		return a1 + (i - 1)*d;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public IRatingIDs deepClone() {
		return new RatingIDsArithmeticSequence(
				this.getA1(), this.getD());
	}
	
	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof RatingIDsArithmeticSequence)) {
	        return false;
	    }
	    
	    RatingIDsArithmeticSequence otherSequence = (RatingIDsArithmeticSequence)other;
	    
	    return getA1() == otherSequence.getA1() &&
	    		getD() == otherSequence.getD();
	}

}
