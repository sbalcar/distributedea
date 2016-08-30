package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.services.ManagerAgentService;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior of {@link Agent_CentralManager} ensures communication with user
 * @author stepan
 *
 */
public class ConsoleAutomatBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batches batches;
	
	private IAgentLogger logger;
	private Agent_CentralManager centralManager;
	
	public ConsoleAutomatBehaviour(Batches batches, IAgentLogger logger) {
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
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		
		logger.log(Level.INFO, "Welcome in DistribudetEA");
		while (true) {
			
			String line = null;
			try {
				line = buffer.readLine();
			} catch (IOException e) {
				logger.logThrowable("Problem with reading from command line", e);
			}
			
			if (line.equals("kill")) {
				logger.log(Level.INFO, "Killing everything");
				
				killCommand();
				
			} else if (line.startsWith("start")) {
				logger.log(Level.INFO, "Starting");
				
				String batchID = line.substring("start".length());
				batchID = batchID.trim();
						
				if (startCommand(batchID)) {
					return;
				}
				
			} else {
				logger.log(Level.INFO, "I don't understand you \n" + 
						"   start - Starting computing \n" +
						"   kill  - Killing everything");
			}
			
		}
	}

	protected boolean startCommand(String batchID) {
		
		Batch batch = batches.exportBatch(batchID);
		
		if (batch == null) {
			logger.log(Level.INFO, "Error JobID doesn't exist");
			return false;
		}
		
		Behaviour behaviourCompI =
					new ComputeBatchesBehaviour(new Batches(batch), logger);
		
		centralManager.computingBehaviours.add(behaviourCompI);
		centralManager.addBehaviour(behaviourCompI);
		
		
		Behaviour behaviorNext = new ConsoleAutomatBehaviour(batches, logger);
		centralManager.addBehaviour(behaviorNext);
		
		return true;
	}
	
	protected void killCommand() {
		
		Agent_DistributedEA agent = (Agent_DistributedEA) myAgent;
		
		AID [] aidManagerAgents = agent.searchDF(
				Agent_ManagerAgent.class.getName());
		
		for (AID aManagerI : aidManagerAgents) {
			ManagerAgentService.sendKillContainer(agent, aManagerI, logger);
		}
	}
	
}
