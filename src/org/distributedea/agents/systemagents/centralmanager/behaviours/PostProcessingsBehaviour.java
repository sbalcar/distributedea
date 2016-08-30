package org.distributedea.agents.systemagents.centralmanager.behaviours;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.logging.TrashLogger;

import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior of {@link Agent_CentralManager} ensures running all
 * {@link PostProcessing} of given {@link Batch}
 * @author stepan
 *
 */
public class PostProcessingsBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batch batch;
	private Agent_CentralManager centralManager;
	
	/**
	 * Constructor
	 * @param batch
	 */
	public PostProcessingsBehaviour(Batch batch) {
		if (batch == null || ! batch.valid(new TrashLogger())) {
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
		}
	}

	private void runPostProcessing(Batch batch) throws Exception {
		
		for (PostProcessing postProcI : batch.getPostProcessings()) {			
			postProcI.run(batch);
		}
	}
}
