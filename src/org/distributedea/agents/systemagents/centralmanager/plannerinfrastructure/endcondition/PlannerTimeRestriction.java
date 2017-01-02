package org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition;

import org.distributedea.ontology.iteration.Iteration;


public class PlannerTimeRestriction implements IPlannerEndCondition {
	
	private long countOfReplaning;
	
	@Deprecated
	PlannerTimeRestriction() {} // only for XStream
	
	public PlannerTimeRestriction(long countOfReplaning) {
		
		this.countOfReplaning = countOfReplaning;
	}
	

	public boolean isContinue(long currentIterationNumber) {
		
		return currentIterationNumber < countOfReplaning;
	}


	public Iteration zeroIteration() {
		
		return new Iteration(0, countOfReplaning);
	}
	

	public Iteration iteration(long currentIterationNumber) {
		
		return new Iteration(currentIterationNumber, countOfReplaning);
	}

}
