package org.distributedea.ontology.saveresult;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.iteration.Iteration;

import jade.content.AgentAction;

/**
 * Ontology represents request to save all results of current {@link Iteration}
 * @author stepan
 *
 */
public class SaveResultOfIteration implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ResultOfIteration results;

	@Deprecated
	public SaveResultOfIteration() {} // only for Jade
	
	/**
	 * Constructor
	 * @param results
	 */
	public SaveResultOfIteration(ResultOfIteration results) {
		if (results == null || ! results.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.results = results;
	}
	
	public ResultOfIteration getResults() {
		return results;
	}
	@Deprecated
	public void setResults(ResultOfIteration results) {
		this.results = results;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return results != null;
	}
}
