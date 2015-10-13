package org.distributedea;

/**
 * Names of Agents definition
 * @author stepan
 *
 */
public enum AgentNames {

	INITIATOR("Initiator"), CENTRAL_LOGER("CentralLoger"), MANAGER_AGENT("ManagerAgent");
	

	private final String name;

	private AgentNames(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
