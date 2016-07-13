package org.distributedea.agents.systemagents.centralmanager.planner.modes;


public class Iteration {
	
	long iterationNumber;
	long expectetMaxIterationNumber;
	
	@Deprecated
	public Iteration() {} // only for XStream(private) and Jade(public)
	
	public Iteration(long iterationNumber, long expectetMaxIterationNumber) {
		
		this.iterationNumber = iterationNumber;
		this.expectetMaxIterationNumber = expectetMaxIterationNumber;
	}

	public long getIterationNumber() {
		return iterationNumber;
	}
	@Deprecated
	public void setIterationNumber(long iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	public long getExpectetMaxIterationNumber() {
		return expectetMaxIterationNumber;
	}
	@Deprecated
	public void setExpectetMaxIterationNumber(long expectetMaxIterationNumber) {
		this.expectetMaxIterationNumber = expectetMaxIterationNumber;
	}
	
	public double exportRationOfIteration() {
		
		return (double) getIterationNumber() / (double) getExpectetMaxIterationNumber();
	}
	
	public Iteration exportPreviousIteration_() {
		if (iterationNumber == 0) {
			return null;
		}
		return new Iteration(iterationNumber - 1, expectetMaxIterationNumber);
	}
	public String exportNumOfIteration() {
		
		String iterationString = String.valueOf(iterationNumber);
		String maxIterationString = String.valueOf(expectetMaxIterationNumber);
		
		while (iterationString.length() < maxIterationString.length()) {
			iterationString = "0" + iterationString;
		}
		
		return iterationString;
	}
	public String exportIterationToString() {
		return exportNumOfIteration() + "-" + getExpectetMaxIterationNumber();
	}
	public static Iteration importIterationToString(String iterationString) {
		int indexOfDelimiter = iterationString.indexOf("-");
		
		String num1 = iterationString.substring(0, indexOfDelimiter);
		String num2 = iterationString.substring(indexOfDelimiter +1, iterationString.length());
		
		long iterationNum = Integer.parseInt(num1);
		long expectetMaxIterationNum = Integer.parseInt(num2);
		
		return new Iteration(iterationNum, expectetMaxIterationNum);
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
}
