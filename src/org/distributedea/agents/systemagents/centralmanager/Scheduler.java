package org.distributedea.agents.systemagents.centralmanager;

import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.ontology.management.agent.Argument;

import jade.core.AID;

public class Scheduler {

	public void run(Agent_CentralManager centramManager, AID [] aidManagerAgents, AgentConfiguration [] configurations) {
	
		for (AID aidI : aidManagerAgents) {
			
			AgentConfiguration agentConfiguration = configurations[0];
			String agentType = agentConfiguration.getAgentType();
			String agentName = agentConfiguration.getAgentName();
			List<Argument> arguments = agentConfiguration.getArguments();
			
			ManagerAgentService.sendCreateAgent(centramManager, aidI, agentType, agentName, arguments);
		}
		
	}
}
