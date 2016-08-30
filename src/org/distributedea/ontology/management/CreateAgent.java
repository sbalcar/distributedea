package org.distributedea.ontology.management;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;

import jade.content.AgentAction;

/**
 * Ontology represent request for creating new agent
 * @author stepan
 *
 */
public class CreateAgent implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private InputAgentConfiguration configuration;

	@Deprecated
	public CreateAgent() {} // only for Jade
	
	/**
	 * Constructor
	 * @param configuration
	 */
	public CreateAgent(InputAgentConfiguration configuration) {
		if (configuration == null || ! configuration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.configuration = configuration;
	}
	
	
	public InputAgentConfiguration getConfiguration() {
		return configuration;
	}
	@Deprecated
	public void setConfiguration(InputAgentConfiguration configuration) {
		if (configuration == null || ! configuration.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.configuration = configuration;
	}
	
}
