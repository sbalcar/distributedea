package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.io.File;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.logging.TrashLogger;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior of {@link Agent_CentralManager} ensures running all
 * {@link PostProcessing} of given {@link Batch}
 * @author stepan
 *
 */
public class ComputePostProcessingsBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batch batch;
	private Agent_CentralManager centralManager;
	
	/**
	 * Constructor
	 * @param batch
	 */
	public ComputePostProcessingsBehaviour(Batch batch) {
		if (batch == null || ! batch.valid(new TrashLogger())) {
			 batch.valid(new TrashLogger());
			throw new IllegalArgumentException("Argument " +
					Batch.class.getSimpleName() + " is not valid");
		}
		this.batch = batch;
	}
	
	@Override
	public void action() {

		if (!(myAgent instanceof Agent_CentralManager)) {
			return;
		}
		this.centralManager = (Agent_CentralManager) myAgent;
		
		try {
			runPostProcessing(batch);
		} catch (Exception e) {
			centralManager.getLogger().logThrowable("PostProc error", e);
			e.printStackTrace();
		}
	}

	private void runPostProcessing(Batch batch) throws Exception {
		
		for (PostProcessing postProcI : batch.getPostProcessings()) {
			
			FilesystemInitTool.moveInputPostProcToResultDir(postProcI.getClass(), batch.getBatchID());
			
			postProcI.run(batch);
		}
		
		File batchesDir = new File(FileNames.getDirectoryOfInputBatches());
		Behaviour behaviour = new ProcessBatchesInInputQueueBehaviour(batchesDir, centralManager.getLogger());
		centralManager.computingBehaviours.add(behaviour);
		centralManager.addBehaviour(behaviour);
	}
}
