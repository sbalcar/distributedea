package org.distributedea.agents.systemagents.centralmanager.scheduler.models;

public class Iteration {
	
	final long iterationNumber;
	final long expectetMaxIterationNumber;
	
	public Iteration(long iterationNumber, long expectetMaxIterationNumber) {
		
		this.iterationNumber = iterationNumber;
		this.expectetMaxIterationNumber = expectetMaxIterationNumber;
	}

	public long getIterationNumber() {
		return iterationNumber;
	}

	public long getExpectetMaxIterationNumber() {
		return expectetMaxIterationNumber;
	}
	
	public double exportRationOfIteration() {
		
		return (double) getIterationNumber() / (double) getExpectetMaxIterationNumber();
	}
}
