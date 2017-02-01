package org.distributedea.agents.systemagents.centralmanager.consoleautomat;

import jade.core.behaviours.Behaviour;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.behaviours.ProcessBatchesInInputQueueBehaviour;
import org.distributedea.logging.IAgentLogger;

public class ConsoleAutomatThread extends Thread {

	private Agent_CentralManager centralManager;
	private File batchesDir;
	private IAgentLogger logger;

	/**
	 * Constructor
	 * @param centralManager
	 * @param logger
	 */
	public ConsoleAutomatThread(Agent_CentralManager centralManager,
			File batchesDir, IAgentLogger logger) {
		
		this.centralManager = centralManager;
		this.batchesDir = batchesDir;
		this.logger = logger;
	}
	
    public void run() {
    	
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
				
				centralManager.exit();

			} else if (line.equals("stop")) {
				logger.log(Level.INFO, "Stoping everything");
				
				centralManager.stopComputation();

			} else if (line.startsWith("start")) {
				logger.log(Level.INFO, "Starting");
				
				//String batchID = line.substring("start".length());
				//batchID = batchID.trim();
						
				if (startCommand()) {
					return;
				}
				
			} else {
				logger.log(Level.INFO, "I don't understand you \n" + 
						"   start - Starting computing \n" +
						"   stop  - Stoping computing \n" +
						"   kill  - Killing everything");
			}
			
		}

    }
    
    
	protected boolean startCommand() {
		
		Behaviour behaviourCompI =
					new ProcessBatchesInInputQueueBehaviour(this.batchesDir, logger);
		
		centralManager.computingBehaviours.add(behaviourCompI);
		centralManager.addBehaviour(behaviourCompI);
		
		return true;
	}
    
}
