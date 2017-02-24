package org.distributedea.ontology.computing;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
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
	
	private IslandModelConfiguration islandModelConfiguration;
	

	@Deprecated
	public StartComputing() {} // only for Jade
	
	/**
	 * Constructor
	 * @param problemWrapper
	 */
	public StartComputing(ProblemWrapper problemWrapper,
			IslandModelConfiguration islandModelConfiguration) {
		setProblemWrapper(problemWrapper);
		setIslandModelConfiguration(islandModelConfiguration);;
	}
	
	public ProblemWrapper getProblemWrapper() {
		return problemWrapper;
	}
	@Deprecated
	public void setProblemWrapper(ProblemWrapper problemWrapper) {
		if (problemWrapper == null ||
				! problemWrapper.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemWrapper.class.getSimpleName() + " is not valid");
		}
		this.problemWrapper = problemWrapper;
	}

	
	public IslandModelConfiguration getIslandModelConfiguration() {
		return islandModelConfiguration;
	}
	@Deprecated
	public void setIslandModelConfiguration(
			IslandModelConfiguration islandModelConfiguration) {
		if (islandModelConfiguration == null ||
				! islandModelConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		this.islandModelConfiguration = islandModelConfiguration;
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