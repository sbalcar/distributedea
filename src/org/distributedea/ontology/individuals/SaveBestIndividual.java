package org.distributedea.ontology.individuals;

import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;

import jade.content.AgentAction;

/**
 * Ontology represents request to save {@link Individual} as the best created
 * individual in current {@link Iteration}.
 * @author stepan
 *
 */
public class SaveBestIndividual implements AgentAction {

	private static final long serialVersionUID = 1L;

	private Iteration iteratin;
	
	private IndividualWrapper result;

	@Deprecated
	public SaveBestIndividual() {}
	
	/**
	 * Constructor
	 * @param iteratin
	 * @param bestIndividual
	 */
	public SaveBestIndividual(Iteration iteratin,
			IndividualWrapper bestIndividual) {
		this.iteratin = iteratin;
		this.result = bestIndividual;
	}
	
	public Iteration getIteratin() {
		return iteratin;
	}
	@Deprecated
	public void setIteratin(Iteration iteratin) {
		this.iteratin = iteratin;
	}

	public IndividualWrapper getResult() {
		return result;
	}
	@Deprecated
	public void setResult(IndividualWrapper result) {
		this.result = result;
	}
	
}
