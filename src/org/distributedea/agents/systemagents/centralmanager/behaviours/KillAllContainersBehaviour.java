package org.distributedea.agents.systemagents.centralmanager.behaviours;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.services.ManagerAgentService;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior of {@link Agent_CentralManager} ensures killing all Jade containers
 * @author stepan
 *
 */
public class KillAllContainersBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private IAgentLogger logger;
	private Agent_CentralManager centralManager;
	
	/**
	 * Constructor
	 * @param logger
	 */
	public KillAllContainersBehaviour(IAgentLogger logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		if (!(myAgent instanceof Agent_CentralManager)) {
			return;
		}
		this.centralManager = (Agent_CentralManager) myAgent;
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		for (AID managerAgentAIDI : aidManagerAgents) {
			
			ManagerAgentService.sendKillContainer(centralManager,
					managerAgentAIDI, logger);
		}
	}

}
