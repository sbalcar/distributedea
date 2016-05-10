package org.distributedea.agents.systemagents.centralmanager.scheduler;


import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.job.noontology.Job;

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
	public abstract void agentInitialization(Agent_CentralManager centralManager,
			Job job, AgentConfigurations configurations,
			AgentLogger logger) throws SchedulerException;
	
	
	/**
	 * Replan computing agents on distributed nodes
	 * @param centramManager
	 * @param problemTool
	 * @param logger
	 */
	public abstract void replan(Agent_CentralManager centralManager,
			Job job, AgentConfigurations configurations,
			AgentLogger logger) throws SchedulerException;
	
	/**
	 * Exit
	 */
	public abstract void exit(Agent_CentralManager centralManager, AgentLogger logger);
	
}