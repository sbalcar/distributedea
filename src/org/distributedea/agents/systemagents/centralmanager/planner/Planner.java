package org.distributedea.agents.systemagents.centralmanager.planner;


import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.job.JobRun;

/**
 * Represents central planner of distributed evolution compute
 * @author stepan
 *
 */
public interface Planner {
	
	/**
	 * Initialize computing agents on distributed nodes
	 * @param centralManager
	 * @param configurations
	 * @param logger
	 */
	public abstract void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws PlannerException;
	
	
	/**
	 * Replan computing agents on distributed nodes
	 * @param centramManager
	 * @param problemTool
	 * @param logger
	 */
	public abstract void replan(Agent_CentralManager centralManager,
			JobRun job, Iteration iteration, ReceivedData receivedData,
			AgentLogger logger) throws PlannerException;
	
	/**
	 * Exit
	 */
	public abstract void exit(Agent_CentralManager centralManager, AgentLogger logger);
	
}