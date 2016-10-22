package org.distributedea;

/**
 * Names of Agents definition
 * @author stepan
 *
 */
public enum AgentNames {

	INITIATOR("Initiator"), CENTRAL_LOGER("CentralLogger"),
	MANAGER_AGENT("ManagerAgent"), DATA_MANAGER("DataManager"),
	MONITOR("Monitor");

	private final String name;

	private AgentNames(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
