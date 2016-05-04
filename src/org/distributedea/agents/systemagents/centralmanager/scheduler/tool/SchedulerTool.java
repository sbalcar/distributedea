package org.distributedea.agents.systemagents.centralmanager.scheduler.tool;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.problem.Problem;

public class SchedulerTool {

	public static List<NodeInfo> getAvailableNodes(Agent_CentralManager centralManager, AgentLogger logger) {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.requestForNodeInfo(
					centralManager, managerAidI, logger);
			nodeInfos.add(nodeInfoI);
		}
		
		return nodeInfos;
	}
	
	public static void killAndCreateAgent(Agent_CentralManager centralManager, AID worstAID,
			AgentConfiguration bestConfiguration, Problem problem, Class<?> problemTool, String jobID, AgentLogger logger) throws SchedulerException {

		
		// kill worst agent
		ManagerAgentService.sendKillAgent(centralManager, worstAID, logger);
	
		// wait for kill and unregister agent
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.logThrowable("Error by waiting for killing agent " + worstAID.getLocalName(), e);
			throw new SchedulerException("Error by waiting for killing agent " + worstAID.getLocalName());
		}

		AID manager = ManagerAgentService.getManagerAgentOfAID(centralManager, worstAID);
				
		// create new agent
		AID newAgent = ManagerAgentService.sendCreateAgent(centralManager,
				manager, bestConfiguration, logger);
		
		// wait for initialization agent
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.logThrowable("Error by waiting for new agent initialization " + newAgent.getLocalName(), e);
			throw new SchedulerException("Error by waiting for new agent initialization " + newAgent.getLocalName());
		}
		
		// start computing
		ComputingAgentService.sendStartComputing(
				centralManager, newAgent, problem, problemTool, jobID, logger);
	}
	
	public static void killAllComputingAgent(Agent_CentralManager centralManager, AgentLogger logger) {
		
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		for (AID compAID : aidComputingAgents) {
		
			// kill agent
			ManagerAgentService.sendKillAgent(centralManager, compAID, logger);
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.logThrowable("", e);
		}
	}
}
