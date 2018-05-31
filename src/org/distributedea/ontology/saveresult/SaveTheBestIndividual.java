package org.distributedea.ontology.saveresult;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;

import jade.content.AgentAction;

/**
 * Ontology represents request to save {@link Individual} as the best created
 * individual in current {@link Iteration}.
 * @author stepan
 *
 */
public class SaveTheBestIndividual implements AgentAction {

	private static final long serialVersionUID = 1L;

	private Iteration iteratin;
	
	private IndividualWrapper result;

	@Deprecated
	public SaveTheBestIndividual() {}  // only for JADE
	
	/**
	 * Constructor
	 * @param iteratin
	 * @param bestIndividual
	 */
	public SaveTheBestIndividual(Iteration iteratin,
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
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return iteratin.valid(logger) &&
				result != null;
	}
	
}