package org.distributedea.agents.systemagents.centralmanager.schedulertype;

import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.ontology.job.JobRun;

public interface SchedulerType {

	public void run(Scheduler scheduler, JobRun jobRun) throws SchedulerException;
}
