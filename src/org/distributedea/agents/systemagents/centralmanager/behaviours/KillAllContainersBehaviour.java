package org.distributedea.agents.systemagents.centralmanager.behaviours;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

public class KillAllContainersBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private AgentLogger logger;
	
	public KillAllContainersBehaviour(AgentLogger logger) {
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		for (AID managerAgentAIDI : aidManagerAgents) {
			
			ManagerAgentService.sendKillContainer(centralManager, managerAgentAIDI, logger);
		}
	}

}
