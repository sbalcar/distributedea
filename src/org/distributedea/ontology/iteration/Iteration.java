package org.distributedea.ontology.iteration;

import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

/**
 * Ontology for identify one iteration of Planner re-planning
 * @author stepan
 *
 */
public class Iteration implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	long iterationNumber;
	long expectetMaxIterationNumber;
	
	@Deprecated
	public Iteration() {} // only for XStream(private) and Jade(public)
	
	/**
	 * Constructor
	 * @param iterationNumber
	 * @param expectetMaxIterationNumber
	 */
	public Iteration(long iterationNumber, long expectetMaxIterationNumber) {
		
		this.iterationNumber = iterationNumber;
		this.expectetMaxIterationNumber = expectetMaxIterationNumber;
	}

	/**
	 * Returns {@link Iteration} number
	 * @return
	 */
	public long getIterationNumber() {
		return iterationNumber;
	}
	@Deprecated
	public void setIterationNumber(long iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	/**
	 * Returns maximal expected {@link Iteration} number
	 * @return
	 */
	public long getExpectetMaxIterationNumber() {
		return expectetMaxIterationNumber;
	}
	@Deprecated
	public void setExpectetMaxIterationNumber(long expectetMaxIterationNumber) {
		this.expectetMaxIterationNumber = expectetMaxIterationNumber;
	}
	
	/**
	 * Returns the ration of number and expected max numer of {@link Iteration}.
	 * @return
	 */	
	public double exportRationOfIteration() {
		
		return (double) getIterationNumber() / (double) getExpectetMaxIterationNumber();
	}
	
	/**
	 * Returns the previous {@link Iteration}. For number 0 returns null.
	 * @return
	 */
	public Iteration exportPreviousIteration() {
		if (iterationNumber == 0) {
			return null;
		}
		return new Iteration(iterationNumber - 1, expectetMaxIterationNumber);
	}
	/**
	 * Exports number of iteration as String. String contains zero-prefix
	 * ensures identical lexicographical sorting.
	 * @return
	 */
	public String exportNumOfIteration() {
		
		String iterationString = String.valueOf(iterationNumber);
		String maxIterationString = String.valueOf(expectetMaxIterationNumber);
		
		while (iterationString.length() < maxIterationString.length()) {
			iterationString = "0" + iterationString;
		}
		
		return iterationString;
	}
	/**
	 * Exports all information from {@link Itertation} to String
	 * @return
	 */
	public String exportIterationToString() {
		return exportNumOfIteration() + "-" + getExpectetMaxIterationNumber();
	}
	/**
	 * Import String to create {@link Iteration}
	 * @param iterationString
	 * @return
	 */
	public static Iteration importIterationToString(String iterationString) {
		int indexOfDelimiter = iterationString.indexOf("-");
		
		String num1 = iterationString.substring(0, indexOfDelimiter);
		String num2 = iterationString.substring(indexOfDelimiter +1, iterationString.length());
		
		long iterationNum = Integer.parseInt(num1);
		long expectetMaxIterationNum = Integer.parseInt(num2);
		
		return new Iteration(iterationNum, expectetMaxIterationNum);
	}
	
	/**
	 * Test validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (iterationNumber > expectetMaxIterationNumber) {
			return false;
		}
		return true;
	}
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof Iteration)) {
	        return false;
	    }
	    
	    Iteration iterationOuther = (Iteration)other;
	    
	    return getIterationNumber() == iterationOuther.getIterationNumber() &&
	    	getExpectetMaxIterationNumber() == iterationOuther.getExpectetMaxIterationNumber();
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return "" + getIterationNumber() + getExpectetMaxIterationNumber();
	}
	
	/**
	 * Exports clone
	 * @return
	 */
	public Iteration deepClone() {
		return new Iteration(iterationNumber, expectetMaxIterationNumber);
	}
}
