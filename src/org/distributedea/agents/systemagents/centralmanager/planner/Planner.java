package org.distributedea.agents.systemagents.centralmanager.planner;


import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.job.JobRun;

/**
 * Represents central planner of distributed evolution compute
 * @author stepan
 *
 */
public interface Planner {
	
	/**
	 * Initialise computing agents on distributed nodes
	 * @param centralManager
	 * @param configurations
	 * @param logger
	 */
	public abstract Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job, IAgentLogger logger) throws PlannerException;
	
	
	/**
	 * Replan computing agents on distributed nodes
	 * @param centramManager
	 * @param problemTool
	 * @param logger
	 */
	public abstract Pair<Plan,RePlan> replan(Iteration iteration, History history
			) throws PlannerException;
	
	/**
	 * Exit with planning
	 */
	public abstract void exit(Agent_CentralManager centralManager, IAgentLogger logger);
	
}