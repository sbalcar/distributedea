package org.distributedea.agents.systemagents.centralmanager.planner.tool;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.HelpmateList;
import org.distributedea.ontology.helpmate.HelpmatesWrapper;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;


public class PlannerTool {

	/**
	 * Returns information about best computing helpers at all nodes
	 * @param centralManager
	 * @param newStatisticForEachQuery
	 * @param logger
	 * @return
	 */
	public static HelpmatesWrapper getHelpmates(Agent_CentralManager centralManager, boolean newStatisticForEachQuery, AgentLogger logger) {
		
		// search all Computing Agents
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		HelpmatesWrapper helpers = new HelpmatesWrapper();
		
		// going through all computing agents
		for (AID aidComputingAgentI : aidComputingAgents) {
			
			HelpmateList helpmateListI = 
					ComputingAgentService.sendReportHelpmate(
							centralManager, aidComputingAgentI,
							newStatisticForEachQuery, logger);
			helpers.addHelper(helpmateListI);
		}
		return helpers;
	}
	
	/**
	 * Returns information about the current computing result at all nodes
	 * @param centralManager
	 * @param logger
	 * @return
	 */
	public static ResultOfComputingWrapper getResultOfComputings(Agent_CentralManager centralManager, AgentLogger logger) {
		
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		ResultOfComputingWrapper resultsOfComputing = new ResultOfComputingWrapper();
		
		for (AID computingAgentI : aidOfComputingAgents) {
						
			ResultOfComputing resultOfComputingI =
					ComputingAgentService.sendAccessesResult(centralManager, computingAgentI, logger);
			resultsOfComputing.addResultOfComputing(resultOfComputingI);
		}
		
		return resultsOfComputing;
	}
	
	/**
	 * Returns information about all Nodes,
	 * information about Node provides Agent_ManagerAgent
	 * @param centralManager
	 * @param logger
	 * @return
	 */
	public static NodeInfosWrapper getAvailableNodes(Agent_CentralManager centralManager, AgentLogger logger) {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.requestForNodeInfo(
					centralManager, managerAidI, logger);
			nodeInfos.add(nodeInfoI);
		}
		
		NodeInfosWrapper wrapper = new NodeInfosWrapper();
		wrapper.setNodeInfos(nodeInfos);
		
		return wrapper;
	}
	
	/**
	 * Kills Agent and creates the new Agent
	 * @param centralManager
	 * @param agentTokillAID
	 * @param bestConfiguration
	 * @param problemStruct
	 * @param logger
	 * @throws PlannerException
	 */
	public static void killAndCreateAgent(Agent_CentralManager centralManager, AID agentTokillAID,
			AgentConfiguration bestConfiguration, ProblemStruct problemStruct, AgentLogger logger) throws PlannerException {

		
		// kill worst agent
		ManagerAgentService.sendKillAgent(centralManager, agentTokillAID, logger);

		AID manager = ManagerAgentService.getManagerAgentOfAID(centralManager, agentTokillAID);
				
		// create new agent
		AID newAgent = ManagerAgentService.sendCreateAgent(centralManager,
				manager, bestConfiguration, logger);
		
		// start computing
		ComputingAgentService.sendStartComputing(
				centralManager, newAgent, problemStruct, logger);
	}
	
	public static void killAllComputingAgent(Agent_CentralManager centralManager, AgentLogger logger) {
		
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		for (AID compAID : aidComputingAgents) {
		
			// kill agent
			ManagerAgentService.sendKillAgent(centralManager, compAID, logger);
		}
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			logger.logThrowable("", e);
		}
	}
}
