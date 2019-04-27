package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.io.IOException;
import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.input.preprocessing.PreProcessing;
import org.distributedea.logging.IAgentLogger;

import jade.core.behaviours.OneShotBehaviour;

public class ComputePreProcessingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Agent_CentralManager centralManager;
	
	private Batch batch;
	private List<PreProcessing> preProcs;
	private IAgentLogger logger;
	
	/**
	 * Constructor
	 * @param preProcs
	 */
	public ComputePreProcessingBehaviour(Batch batch,
			List<PreProcessing> preProcs, IAgentLogger logger) {
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		if (batch == null || ! batch.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					Batch.class.getSimpleName() + " is not valid");			
		}
		if (preProcs == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		
		this.batch = batch;
		this.preProcs = preProcs;
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		if (!(myAgent instanceof Agent_CentralManager)) {
			return;
		}
		this.centralManager = (Agent_CentralManager) myAgent;
		
		
		for (PreProcessing postPrI : preProcs) {
			try {
				FilesystemInitTool.moveInputPreProcToResultDir(postPrI.getClass(), batch.getBatchID());
			} catch (IOException e) {
				logger.logThrowable("Can not move preprocessing to result directory", e);
				centralManager.exit();
				return;
			}
			try {
				postPrI.run(batch);
			} catch (Exception e) {
				logger.logThrowable("Can not run preprocessing", e);
				centralManager.exit();
				return;

			}
		}
	}

}
