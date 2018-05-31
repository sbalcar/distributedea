package org.distributedea.ontology.saveresult;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.saveresult.resultofiteration.ResultOfIteration;

import jade.content.AgentAction;

/**
 * Ontology represents request to save all results of current {@link Iteration}
 * @author stepan
 *
 */
public class SaveResultOfIteration implements AgentAction {

	private static final long serialVersionUID = 1L;

	private IProblem problem;
	
	private ResultOfIteration results;

	@Deprecated
	public SaveResultOfIteration() {} // only for Jade
	
	/**
	 * Constructor
	 * @param results
	 */
	public SaveResultOfIteration(IProblem problem,
			ResultOfIteration results) {
		setProblem(problem);
		setResults(results);
	}
	
	public IProblem getProblem() {
		return problem;
	}
	@Deprecated
	public void setProblem(IProblem problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
		this.problem = problem;
	}

	public ResultOfIteration getResults() {
		return results;
	}
	@Deprecated
	public void setResults(ResultOfIteration results) {
		if (results == null || ! results.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ResultOfIteration.class.getSimpleName() + " is not valid");
		}
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
