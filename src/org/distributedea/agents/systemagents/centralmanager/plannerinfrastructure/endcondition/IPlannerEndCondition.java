package org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition;

import org.distributedea.ontology.iteration.Iteration;

public interface IPlannerEndCondition {

	public boolean isContinue(long currentIterationNumber);
	
	public Iteration zeroIteration();
	public Iteration iteration(long currentIterationNumber);
	
}
