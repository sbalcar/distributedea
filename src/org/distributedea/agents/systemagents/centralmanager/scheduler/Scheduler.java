package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
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
			Problem problem, List<AgentConfiguration> configurations,
			List<Class<?>> availablProblemTools, AgentLogger logger) throws SchedulerException;
	
	/**
	 * Decide on the continuation of the calculation
	 * @return
	 */
	public boolean continueWithComputingInTheNextGeneration();
	
	/**
	 * Replan computing agents on distributed nodes
	 * @param centramManager
	 * @param problemTool
	 * @param logger
	 */
	public void replan(Agent_CentralManager centralManager,
			Problem problem, List<AgentConfiguration> configurations,
			List<Class<?>> availableProblemTools, AgentLogger logger) throws SchedulerException;
	
	/**
	 * Exit
	 */
	public void exit(Agent_CentralManager centralManager, AgentLogger logger);
	
}