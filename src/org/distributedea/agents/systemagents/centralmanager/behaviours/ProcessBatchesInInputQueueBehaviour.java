package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.IPlannerEndCondition;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.services.CentralLogerService;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior of Computation Agent ensures computation of one {@link Batch}.
 * @author stepan
 *
 */
public class ProcessBatchesInInputQueueBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private File inputBatchesDir;
	
	private IAgentLogger logger;
	private Agent_CentralManager centralManager;
	
	/**
	 * Constructor
	 * @param batches
	 * @param logger
	 */
	public ProcessBatchesInInputQueueBehaviour(File batchesDir, IAgentLogger logger) {
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		if (batchesDir == null || ! batchesDir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}

		this.inputBatchesDir = batchesDir;
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		if (!(myAgent instanceof Agent_CentralManager)) {
			return;
		}
		this.centralManager = (Agent_CentralManager) myAgent;
		
		
		processBatches(inputBatchesDir);
	}
	
	private void processBatches(File batchesDir) {
		
		Batches batches = null;
		try {
			batches = Batches.importXML(batchesDir);
		} catch (Exception e) {
			logger.log(Level.INFO, "Can not read batches");
		}
		
		if (batches == null) {
			centralManager.exit();
			return;
		}
		
				
		if (batches == null || ! batches.containsJobOrPostProc()) {
			
			logger.log(Level.INFO, "Input Batch's Queue is empty");
			CentralLogerService.logMessage(centralManager, "Input Batch's Queue is empty", logger);
			
			boolean automaticExit = true;
			try {
				automaticExit = InputConfiguration.getConf().automaticExit;
			} catch (Exception e) {
			}

			if (automaticExit) {
				centralManager.exit();
			}
			return;
		}
		
		for (Batch batchI : batches.getBatches()) {
			
			if (! batchI.getJobs().isEmpty() ||
					! batchI.getPostProcessings().isEmpty()) {
				processBatch(batchI);
				return; // process only one Batch
			}
		}
	}
	
	private void processBatch(Batch batch) {
		
		String batchID = batch.getBatchID();
		FilesystemInitTool.createResultSpaceForBatch(batchID);
		FilesystemInitTool.copyInputBatchDescriptionToResultDir(batchID);
		
		// add Behavior for Job Runs
		for (Job jobI : batch.getJobs()) {
			
			processJob(jobI, batchID);
			return; // process only one Job
		}

		List<PostProcessing> postProcs = batch.getPostProcessings();
		processPostProc(batchID, postProcs);
	}

	private void processJob(Job job, String batchID) {
		
		FilesystemInitTool.createResultSpaceForJob(batchID, job.getJobID(), logger);
		
		IslandModelConfiguration configuration = job.getIslandModelConfiguration();
		int numberOfRuns = job.getNumberOfRuns();

		IPlannerEndCondition endCondition = job.getPlannerEndCondition();
		IPlanner planner = job.getPlanner();
		
		for (int runNumberI = 0; runNumberI < numberOfRuns; runNumberI++) {
			
			JobRun jobRunI = job.exportJobRun(batchID, runNumberI, logger);

			if (! FilesystemInitTool.existsResultSpaceForJobRun(jobRunI.getJobID())) {
			
				processJobRun(jobRunI, numberOfRuns, configuration,
						endCondition, planner);
				return; //process only one JobRun
			}
		}
	}
	
	private void processJobRun(JobRun jobRun, int numberOfRuns, IslandModelConfiguration configuratiion,
			IPlannerEndCondition endCondition, IPlanner planner) {
		
		ComputeJobRunBehaviour jobRunBehaviourI =
				new ComputeJobRunBehaviour(jobRun, numberOfRuns,
						configuratiion, endCondition, planner, logger);
		
		centralManager.computingBehaviours.add(jobRunBehaviourI);
		centralManager.addBehaviour(jobRunBehaviourI);

	}
	
	private void processPostProc(String batchID, List<PostProcessing> postProcs) {
		
		// reads complete Batch from result directory
		String computedBatchDirName = FileNames.getResultDirectoryWithCopyOfInputBatch(batchID);
		File computedBatchDir = new File(computedBatchDirName);
		
		Batch batch = null;
		try {
			batch = Batch.importXML(computedBatchDir);
		} catch (Exception e) {
			logger.log(Level.INFO, "Can not read computed batches");
			centralManager.exit();
		}
		
		batch.setPostProcessings(postProcs);
		
		// add Behavior for PostProcessings
		Behaviour behaviourI = new ComputePostProcessingsBehaviour(batch);
		centralManager.computingBehaviours.add(behaviourI);
		centralManager.addBehaviour(behaviourI);

	}
}
