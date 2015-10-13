package org.distributedea.agents.systemagents.centralmanager;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.logging.AgentLogger;

/**
 * Represents central planner of distributed evolution compute
 * @author stepan
 *
 */
public interface Scheduler {

	/**
	 * Initialize computing agents on distributed nodes
	 * @param centramManager
	 * @param configurations
	 * @param logger
	 */
	public void agentInitialization(Agent_CentralManager centramManager,
			AgentConfiguration [] configurations, String problemFileName,
			Class<?> [] availablProblemTools, AgentLogger logger);
	
	/**
	 * Replan computing agents on distributed nodes
	 * @param centramManager
	 * @param problemTool
	 * @param logger
	 */
	public void replan(Agent_CentralManager centramManager,
			AgentLogger logger);
}