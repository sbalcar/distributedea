package org.distributedea.ontology.individuals;

import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;

import jade.content.AgentAction;

public class SaveBestIndividual implements AgentAction {

	private static final long serialVersionUID = 1L;

	private Iteration iteratin;
	
	private IndividualWrapper result;

	public SaveBestIndividual() {}
	
	public SaveBestIndividual(Iteration iteratin,
			IndividualWrapper bestIndividual) {
		this.iteratin = iteratin;
		this.result = bestIndividual;
	}
	
	public Iteration getIteratin() {
		return iteratin;
	}
	public void setIteratin(Iteration iteratin) {
		this.iteratin = iteratin;
	}

	public IndividualWrapper getResult() {
		return result;
	}
	public void setResult(IndividualWrapper result) {
		this.result = result;
	}
	
}
