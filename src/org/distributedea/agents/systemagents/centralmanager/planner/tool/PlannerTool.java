package org.distributedea.agents.systemagents.centralmanager.planner.tool;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel.ResultsOfComputing;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.HelpmateList;
import org.distributedea.ontology.helpmate.HelpmatesWrapper;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobRun;
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
	public static HelpmatesWrapper getHelpmates(Agent_CentralManager centralManager,
			boolean newStatisticForEachQuery, IAgentLogger logger) {
		
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
	public static ResultsOfComputing getResultOfComputings(Agent_CentralManager centralManager, AgentLogger logger) {
		
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		ResultsOfComputing resultsOfComputing = new ResultsOfComputing();
		
		for (AID computingAgentI : aidOfComputingAgents) {
						
			IndividualWrapper resultOfComputingI =
					ComputingAgentService.sendAccessesResult(centralManager,
							computingAgentI, logger);
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
	public static NodeInfosWrapper getAvailableNodes(Agent_CentralManager centralManager, IAgentLogger logger) {
		
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
	 * Process Replan
	 * @param centralManager
	 * @param replan
	 * @param jobRun
	 * @param logger
	 * @throws PlannerException
	 */
	public static void processReplanning(Agent_CentralManager centralManager,
			RePlan replan, JobRun jobRun, IAgentLogger logger) throws PlannerException {
		
		List<AgentDescription> agentsToKill = replan.getAgentsToKill();
		List<AgentDescription> agentsToCreate = replan.getAgentsToCreate();
		
		for (int i = 0; i < agentsToKill.size(); i++) {
			
			AgentDescription agentsToKillI = agentsToKill.get(i);
			AgentDescription agentsToCreateI = agentsToCreate.get(i);
			
			AID agentTokillAID = agentsToKillI.exportAgentAID();
			AgentConfiguration newConfiguration =
					agentsToCreateI.getAgentConfiguration();
			
			Class<?> problemToolClass =
					agentsToCreateI.exportProblemToolClass();
			ProblemStruct problemStruct =
					jobRun.exportProblemStruct(problemToolClass);
			
			
			AgentConfiguration createdAC = killAndCreateAgent(centralManager,
					agentTokillAID, newConfiguration, problemStruct, logger);
			
			// update configuration
			newConfiguration.setNumberOfAgent(createdAC.getNumberOfAgent());
			newConfiguration.setContainerID(createdAC.getContainerID());
			newConfiguration.setNumberOfContainer(createdAC.getNumberOfContainer());
		}
		
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
	public static AgentConfiguration killAndCreateAgent(Agent_CentralManager centralManager, AID agentTokillAID,
			AgentConfiguration newConfiguration, ProblemStruct problemStruct, IAgentLogger logger) throws PlannerException {

		
		// kill agent
		ManagerAgentService.sendKillAgent(centralManager, agentTokillAID, logger);

		logger.log(Level.INFO, "Killed: " + agentTokillAID.getLocalName());
		AID manager = ManagerAgentService.getManagerAgentOfAID(centralManager, agentTokillAID);
				
		// create new agent
		AgentConfiguration newAgent = ManagerAgentService.sendCreateAgent(centralManager,
				manager, newConfiguration, logger);
		
		logger.log(Level.INFO, "Created: " + newAgent.exportAgentname());
		
		// start computing
		ComputingAgentService.sendStartComputing(
				centralManager, newAgent.exportAgentAID(), problemStruct, logger);
		return newAgent;
	}
	
	public static void killAllComputingAgent(Agent_CentralManager centralManager, IAgentLogger logger) {
		
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
