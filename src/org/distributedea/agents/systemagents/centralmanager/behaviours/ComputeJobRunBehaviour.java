package org.distributedea.agents.systemagents.centralmanager.behaviours;


import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.schedulertype.SchedulerTypeTimeRestriction;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.job.JobRun;

import jade.core.behaviours.OneShotBehaviour;

public class ComputeJobRunBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private JobRun jobRun;
	private Scheduler scheduler;
	private long countOfReplaning;
	private AgentLogger logger;
	
	public ComputeJobRunBehaviour(JobRun jobRun, Scheduler scheduler,
			long countOfReplaning, AgentLogger logger) {

		this.jobRun = jobRun;
		this.scheduler = scheduler;
		this.countOfReplaning = countOfReplaning;
		this.logger = logger;
	}
	
	@Override
	public void action() {

		// testing validity
		if (logger == null) {
			return;
		}
		
		if (scheduler == null) {
			logger.log(Level.WARNING, "scheduler can not be null");
			return;
		}

		if ((jobRun == null) || (! jobRun.validation())) {
			logger.log(Level.WARNING, "job can not be null");
			return;
		}


		logger.log(Level.INFO, "Computing JobRun behaviour: " + jobRun.getJobID());
		
		try {
			startComputing();
		} catch (SchedulerException e) {
			logger.logThrowable("Computing was stoped", e);
		}
		
	}

	
	protected void startComputing() throws SchedulerException {			
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		
		SchedulerTypeTimeRestriction schedulerType =
				new SchedulerTypeTimeRestriction(centralManager, countOfReplaning, logger);
		
		schedulerType.run(scheduler, jobRun);
	}

}
