package org.distributedea.agents.systemagents.centralmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

public class ConsoleAutomatBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private AgentLogger logger;
	
	public ConsoleAutomatBehaviour(AgentLogger logger) {
		this.logger = logger;
	}
	
	@Override
	public void action() {

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
				
			} else if (line.equals("start")) {
				logger.log(Level.INFO, "Starting everything");
				
				startCommand();
				
			} else {
				logger.log(Level.INFO, "I don't understand you \n" + 
						"   start - Starting computing \n" +
						"   kill  - Killing everything");
			}
			
		}
	}

	protected void startCommand() {
		
		// check if contains Behaviour
		myAgent.addBehaviour(new StartComputingBehaviour(logger));
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
