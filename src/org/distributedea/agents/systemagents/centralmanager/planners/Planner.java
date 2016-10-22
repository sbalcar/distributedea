package org.distributedea.agents.systemagents.centralmanager.planners;


import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;

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
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws Exception;
	
	
	/**
	 * Replan computing agents on distributed nodes
	 * @param centramManager
	 * @param problemTool
	 * @param logger
	 */
	public abstract Pair<Plan,RePlan> replan(Iteration iteration, History history
			) throws Exception;
	
	/**
	 * Exit with planning
	 */
	public abstract void exit(Agent_CentralManager centralManager, IAgentLogger logger);
	
}