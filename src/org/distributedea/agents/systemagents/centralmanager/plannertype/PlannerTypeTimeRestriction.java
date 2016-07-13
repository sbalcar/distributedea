package org.distributedea.agents.systemagents.centralmanager.plannertype;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;

public class PlannerTypeTimeRestriction extends PlannerType {
	
	private long countOfReplaning;
	
	@Deprecated
	PlannerTypeTimeRestriction() {} // only for XStream
	
	public PlannerTypeTimeRestriction(long countOfReplaning) {
		
		this.countOfReplaning = countOfReplaning;
	}
	
	@Override
	protected boolean isContinue() {
		
		return getIterationNumber() < countOfReplaning;
	}


	@Override
	protected Iteration zeroIteration() {
		return new Iteration(0, countOfReplaning);
	}
	
	@Override
	protected Iteration iteration() {
		
		logger.log(Level.INFO, "Replanning: " + getIterationNumber() + " / " + countOfReplaning);
		
		return new Iteration(getIterationNumber(), countOfReplaning);
	}

}
