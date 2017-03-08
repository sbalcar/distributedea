package org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition;

import org.distributedea.ontology.iteration.Iteration;


public class PlannerEndCondIterationCountRestriction implements IPlannerEndCondition {
	
	private long countOfReplanning;
	
	@Deprecated
	PlannerEndCondIterationCountRestriction() {} // only for XStream
	
	public PlannerEndCondIterationCountRestriction(long countOfReplaning) {
		
		this.countOfReplanning = countOfReplaning;
	}
	

	public boolean isContinue(long currentIterationNumber) {
		
		return currentIterationNumber < countOfReplanning;
	}


	public Iteration zeroIteration() {
		
		return new Iteration(0, countOfReplanning);
	}
	

	public Iteration iteration(long currentIterationNumber) {
		
		return new Iteration(currentIterationNumber, countOfReplanning);
	}

	/**
	 * Exports the count of re-planning iterations
	 * @return
	 */
	public long exportCountOfReplanningIterations() {
		return countOfReplanning;
	}
}
