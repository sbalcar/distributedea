package org.distributedea.agents.systemagents.centralmanager.plannertype;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;

public interface IPlannerType {

	public void initialization(Agent_CentralManager agent, JobID jobID,
			IAgentLogger logger);
	public void run(Planner scheduler, JobRun jobRun) throws Exception;
}
