package org.distributedea.ontology.computing;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;

import jade.content.AgentAction;

/**
 * Ontology represents request for start of computing
 * @author stepan
 *
 */
public class StartComputing implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ProblemWrapper problemWrapper;

	@Deprecated
	public StartComputing() {} // only for Jade
	
	/**
	 * Constructor
	 * @param problemWrapper
	 */
	public StartComputing(ProblemWrapper problemWrapper) {
		if (problemWrapper == null || ! problemWrapper.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.problemWrapper = problemWrapper;
	}
	
	public ProblemWrapper getProblemWrapper() {
		return problemWrapper;
	}
	@Deprecated
	public void setProblemWrapper(ProblemWrapper problemWrapper) {
		if (problemWrapper == null || ! problemWrapper.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.problemWrapper = problemWrapper;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (problemWrapper == null || ! problemWrapper.valid(logger)) {
			return false;
		}
		return true;
	}
}