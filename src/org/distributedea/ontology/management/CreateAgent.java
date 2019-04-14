package org.distributedea.ontology.management;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;

import jade.content.AgentAction;

/**
 * Ontology represent request for creating new agent
 * @author stepan
 *
 */
public class CreateAgent implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private InputAgentConfiguration inputAgentConf;

	private MethodIDs methodIDs;
	
	
	@Deprecated
	public CreateAgent() {} // only for Jade
	
	/**
	 * Constructor
	 * @param inputAgentConf
	 */
	public CreateAgent(InputAgentConfiguration inputAgentConf, MethodIDs methodIDs) {

		setConfiguration(inputAgentConf);
		setMethodIDs(methodIDs);
	}
	
	
	public InputAgentConfiguration getConfiguration() {
		return inputAgentConf;
	}
	@Deprecated
	public void setConfiguration(InputAgentConfiguration inputAgentConf) {
		if (inputAgentConf == null || ! inputAgentConf.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfiguration.class.getSimpleName() + " is not valid");
		}
		this.inputAgentConf = inputAgentConf;
	}

	
	public MethodIDs getMethodIDs() {
		return methodIDs;
	}
	@Deprecated
	public void setMethodIDs(MethodIDs methodIDs) {
		if (methodIDs == null || ! methodIDs.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodIDs.class.getSimpleName() + " is not valid");
		}
		this.methodIDs = methodIDs;
	}

	
}
