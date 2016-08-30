package org.distributedea.ontology.management;

import jade.content.AgentAction;
import jade.core.AID;

/**
 * Ontology represents request to kill agent
 * @author stepan
 *
 */
public class KillAgent implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private String agentName;

	@Deprecated
	public KillAgent() {} // only for Jade
	
	/**
	 * Constructor
	 * @param agentAID
	 */
	public KillAgent(AID agentAID) {
		if (agentAID == null) {
			throw new IllegalArgumentException();
		}
		importAgent(agentAID);
	}
	
	public String getAgentName() {
		return agentName;
	}
	@Deprecated
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	/**
	 * Imports agent to kill
	 * @param agentAID
	 */
	public void importAgent(AID agentAID) {
		if (agentAID == null) {
			throw new IllegalArgumentException();
		}
		this.agentName = agentAID.getLocalName();
	}
}
