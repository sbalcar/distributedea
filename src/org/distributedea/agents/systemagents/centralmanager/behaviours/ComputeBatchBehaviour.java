package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

public class ComputeBatchBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batch batch;
	private Behaviour nextBehavior;
	private AgentLogger logger;
	
	public ComputeBatchBehaviour(Batch batch, Behaviour nextBehavior, AgentLogger logger) {
		this.batch = batch;
		this.nextBehavior = nextBehavior;
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		String batchID = batch.getBatchID();
		
		//copy input
		FilesystemTool.createResultSpaceForBatch(batchID);
		FilesystemTool.copyBatchDescriptionToResultDir(batchID);
		

		// add Behavior for Job Runs
		List<Job> jobs = batch.getJobs();
		processJobs(jobs, batchID);


		// add Behavior for PostProcessings
		processPostProcessings(batch);
		
		// add the next Behavior
		if (nextBehavior != null) {
			this.myAgent.addBehaviour(nextBehavior);
		}
	}

	private void processJobs(List<Job> jobs, String batchID) {
		
		// add Behavior for Jobs
		for (Job jobI : jobs) {
			
			try {
				processJob(jobI, batchID);
			} catch (PlannerException e) {
				logger.logThrowable("Can't execute Job", e);
			}
		}
	}
	
	private void processJob(Job job, String batchID) throws PlannerException {
		

		int numberOfRuns = job.getNumberOfRuns();
		long countOfReplaning = job.getCountOfReplaning();
		Planner scheduler = job.getPlanner();
		

		for (int runNumberI = 0; runNumberI < numberOfRuns; runNumberI++) {
			
			JobRun jobRunI = job.exportJobRun(batchID, runNumberI, logger);

			ComputeJobRunBehaviour jobBehaviour =
					new ComputeJobRunBehaviour(jobRunI, scheduler, countOfReplaning, logger);
			this.myAgent.addBehaviour(jobBehaviour);
		}
		
	}

	private void processPostProcessings(Batch batch) {
		
		Behaviour behaviour = new PostProcessingsBehaviour(batch);
		this.myAgent.addBehaviour(behaviour);

	}
}
