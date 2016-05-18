package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.util.List;

import org.distributedea.agents.systemagents.Agent_DataManager;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

import jade.core.behaviours.OneShotBehaviour;

public class BatchComputingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batch batch;
	private AgentLogger logger;
	
	public BatchComputingBehaviour(Batch batch, AgentLogger logger) {
		this.batch = batch;
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		List<JobWrapper> jobWrps = batch.getJobWrappers();
		String batchID = batch.getBatchID();
		
		for (JobWrapper JobWrpI : jobWrps) {
			
			String jobID = JobWrpI.getJobID();
			
			JobID jobIDS = new JobID(batchID, jobID);
			Agent_DataManager.createLogSpaceForJob(jobIDS);
			Agent_DataManager.createResultSpaceForJob(jobIDS);
			
			JobComputingBehaviour jobBehaviour = new JobComputingBehaviour(JobWrpI, batchID, logger);
			this.myAgent.addBehaviour(jobBehaviour);
		}
		
	}

}
