package org.distributedea.agents.systemagents.centralmanager.behaviours;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.IPlannerEndCondition;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.job.JobRun;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior of Computation Agent ensures computation of one {@link Batch}.
 * @author stepan
 *
 */
public class ComputeBatchesBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batches batches;
	
	private IAgentLogger logger;
	private Agent_CentralManager centralManager;
	
	/**
	 * Constructor
	 * @param batches
	 * @param logger
	 */
	public ComputeBatchesBehaviour(Batches batches, IAgentLogger logger) {
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		if (batches == null || ! batches.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					Batches.class.getSimpleName() + " is not valid");
		}
		this.batches = batches;
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		if (!(myAgent instanceof Agent_CentralManager)) {
			return;
		}
		this.centralManager = (Agent_CentralManager) myAgent;
		
		processBatches(batches);
		
		Behaviour behaviourKill =
				new KillAllContainersBehaviour(logger);
		this.myAgent.addBehaviour(behaviourKill);
	}
	
	private void processBatches(Batches batches) {
		
		for (Batch batchI : batches.getBatches()) {
			processBatch(batchI);
		}
	}
	
	private void processBatch(Batch batch) {
		
		String batchID = batch.getBatchID();
		

		// add Behavior for Job Runs
		for (Job jobI : batch.getJobs()) {
			
			FilesystemInitTool.clearResultSpaceForJob(batchID, jobI.getJobID(), logger);
			
			processJob(jobI, batchID);
		}

		// add Behavior for PostProcessings
		Behaviour behaviourI = new PostProcessingsBehaviour(batch);
		centralManager.computingBehaviours.add(behaviourI);
		centralManager.addBehaviour(behaviourI);

	}

	private void processJob(Job job, String batchID) {
		
		int numberOfRuns = job.getNumberOfRuns();

		IPlannerEndCondition endCondition = job.getPlannerEndCondition();
		IPlanner planner = job.getPlanner();
		
		for (int runNumberI = 0; runNumberI < numberOfRuns; runNumberI++) {
			
			JobRun jobRunI = job.exportJobRun(batchID, runNumberI, logger);

			ComputeJobRunBehaviour jobRunBehaviourI =
					new ComputeJobRunBehaviour(jobRunI, endCondition, planner, logger);
			centralManager.computingBehaviours.add(jobRunBehaviourI);
			centralManager.addBehaviour(jobRunBehaviourI);
		}
		
	}
}
