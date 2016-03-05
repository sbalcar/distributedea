package org.distributedea.agents.systemagents.centralmanager.scheduler;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.problem.Problem;

/**
 * Represents central planner of distributed evolution compute
 * @author stepan
 *
 */
public interface Scheduler {

	/**
	 * Initialize computing agents on distributed nodes
	 * @param centralManager
	 * @param configurations
	 * @param logger
	 */
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, AgentConfiguration [] configurations,
			Class<?> [] availablProblemTools, AgentLogger logger);
	
	/**
	 * Replan computing agents on distributed nodes
	 * @param centramManager
	 * @param problemTool
	 * @param logger
	 */
	public void replan(Agent_CentralManager centralManager,
			Problem problem, AgentConfiguration [] configurations,
			Class<?> []  availableProblemTools, AgentLogger logger);
}