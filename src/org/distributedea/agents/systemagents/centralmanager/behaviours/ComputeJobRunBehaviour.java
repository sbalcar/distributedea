package org.distributedea.agents.systemagents.centralmanager.behaviours;


import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.plannertype.PlannerTypeTimeRestriction;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.job.JobRun;

import jade.core.behaviours.OneShotBehaviour;

public class ComputeJobRunBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private JobRun jobRun;
	private Planner planner;
	private long countOfReplaning;
	private AgentLogger logger;
	
	public ComputeJobRunBehaviour(JobRun jobRun, Planner scheduler,
			long countOfReplaning, AgentLogger logger) {

		this.jobRun = jobRun;
		this.planner = scheduler;
		this.countOfReplaning = countOfReplaning;
		this.logger = logger;
	}
	
	@Override
	public void action() {

		// testing validity
		if (logger == null) {
			return;
		}
		
		if (planner == null) {
			logger.log(Level.WARNING, "Planner can not be null");
			return;
		}

		if ((jobRun == null) || (! jobRun.validation())) {
			logger.log(Level.WARNING, "job can not be null");
			return;
		}


		logger.log(Level.INFO, "Computing JobRun behaviour: " + jobRun.getJobID());
		
		try {
			startComputing();
		} catch (PlannerException e) {
			logger.logThrowable("Computing was stoped", e);
		}
		
	}

	
	protected void startComputing() throws PlannerException {			
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		
		PlannerTypeTimeRestriction plannerType =
				new PlannerTypeTimeRestriction(centralManager, countOfReplaning, logger);
		
		plannerType.run(planner, jobRun);
	}

}
