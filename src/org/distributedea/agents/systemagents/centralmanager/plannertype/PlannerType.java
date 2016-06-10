package org.distributedea.agents.systemagents.centralmanager.plannertype;

import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.ontology.job.JobRun;

public interface PlannerType {

	public void run(Planner scheduler, JobRun jobRun) throws PlannerException;
}
