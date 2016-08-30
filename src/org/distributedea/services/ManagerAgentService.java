package org.distributedea.services;

import java.util.ArrayList;
import java.util.List;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.CreatedAgent;
import org.distributedea.ontology.management.KillAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.computingnode.DescribeNode;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;

/**
 * Wrapper for all services which provides {@link Agent_ManagerAgent}
 * @author stepan
 *
 */
public class ManagerAgentService {

	/**
	 * Returns information about all Nodes,
	 * information about Node provides {@link Agent_ManagerAgent}
	 * @param centralManager
	 * @param logger
	 * @return
	 */
	public static NodeInfosWrapper getAvailableNodes(Agent_CentralManager agentSender, IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		AID [] aidManagerAgents = agentSender.searchDF(
				Agent_ManagerAgent.class.getName());
		
		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.requestForNodeInfo(
					agentSender, managerAidI, logger);
			nodeInfos.add(nodeInfoI);
		}
		
		return new NodeInfosWrapper(nodeInfos);
	}
	
	/**
	 * Sends request to information about node
	 * 
	 * @param agentSender
	 * @param agentReciever
	 * @param logger
	 * @return
	 */
	public static NodeInfo requestForNodeInfo(Agent_DistributedEA agentSender,
			AID agentReciever, IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (agentReciever == null) {
			throw new IllegalArgumentException("Argument " + 
					AID.class.getSimpleName() + " can't be null");
		}
		
		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgDescribeNode = new ACLMessage(ACLMessage.REQUEST);
		msgDescribeNode.addReceiver(agentReciever);
		msgDescribeNode.setSender(agentSender.getAID());
		msgDescribeNode.setLanguage(agentSender.getCodec().getName());
		msgDescribeNode.setOntology(ontology.getName());

		DescribeNode describeNode = new DescribeNode();
		
		Action action = new Action(agentSender.getAID(), describeNode);
		
		try {
			agentSender.getContentManager().fillContent(msgDescribeNode, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " + DescribeNode.class.getSimpleName(), e);
			return null;
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " + DescribeNode.class.getSimpleName(), e);
			return null;
		}

		ACLMessage nodeInfoDescription = null;
		try {
			nodeInfoDescription = FIPAService
					.doFipaRequestClient(agentSender, msgDescribeNode);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to " + DescribeNode.class.getSimpleName(), e);
			return null;
		}
		
		NodeInfo nodeInfo = null;
		try {
			Result result = (Result) agentSender.getContentManager()
					.extractContent(nodeInfoDescription);

			nodeInfo = (NodeInfo) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving " + NodeInfo.class.getSimpleName(), e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving " + NodeInfo.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving " + NodeInfo.class.getSimpleName(), e);
		}

		return nodeInfo;
	}
	
	
	/**
	 * Sends request to create Agent
	 * 
	 * @param agentSender
	 * @param agentReciever
	 * @param agentType
	 * @param agentName
	 * @param arguments
	 * @return
	 */
	public static AgentConfiguration sendCreateAgent(Agent_DistributedEA agentSender,
			AID agentReciever, InputAgentConfiguration agentConfiguration,
			IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (agentReciever == null) {
			throw new IllegalArgumentException("Argument " +
					AID.class.getSimpleName() + " can't be null");
		}
		if (agentConfiguration == null || ! agentConfiguration.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		
		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgCreateA = new ACLMessage(ACLMessage.REQUEST);
		msgCreateA.addReceiver(agentReciever);
		msgCreateA.setSender(agentSender.getAID());
		msgCreateA.setLanguage(agentSender.getCodec().getName());
		msgCreateA.setOntology(ontology.getName());

		CreateAgent createAgent = new CreateAgent(agentConfiguration);
		
		Action action = new Action(agentSender.getAID(), createAgent);
		
		try {
			agentSender.getContentManager().fillContent(msgCreateA, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " + CreateAgent.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " + CreateAgent.class.getSimpleName(), e);
		}

		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agentSender, msgCreateA);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to " + CreatedAgent.class.getSimpleName(), e);
			return null;
		}

		CreatedAgent createdAgent = null;
		try {
			Result result = (Result) agentSender.getContentManager()
					.extractContent(msgRetursName);

			createdAgent = (CreatedAgent) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving " + CreatedAgent.class.getSimpleName(), e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving " + CreatedAgent.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving " + CreatedAgent.class.getSimpleName(), e);
		}
		
		return createdAgent.getCreatedAgent();
		
	}
	
	/**
	 * Kills all {@link Agent_ComputingAgent}s
	 * @param centralManager
	 * @param logger
	 */
	public static void killAllComputingAgent(Agent_DistributedEA agentSender,
			IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}

		AID [] aidComputingAgents = agentSender.searchDF(
				Agent_ComputingAgent.class.getName());
		
		for (AID compAID : aidComputingAgents) {
		
			// kill agent
			ManagerAgentService.sendKillAgent(agentSender, compAID, logger);
		}
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			logger.logThrowable("", e);
		}
	}
	
	/**
	 * Sends request to kill agent
	 * 
	 * @param agentSender
	 * @param logger
	 * @return
	 */
	public static boolean sendKillAgent(Agent_DistributedEA agentSender, AID agentAID,
			IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException(
					"Argument logger can't be null");
		}

		
		AID aidManagerAgent = getManagerAgentOfAID(agentSender, agentAID);
		if (aidManagerAgent == null) {
			throw new IllegalStateException(
					"Agent ManagerAgents doesn't exist");
		}
		
		Ontology ontology = ManagementOntology.getInstance();
		
		ACLMessage msgKillAgent = new ACLMessage(ACLMessage.REQUEST);
		msgKillAgent.addReceiver(aidManagerAgent);
		msgKillAgent.setSender(agentSender.getAID());
		msgKillAgent.setLanguage(agentSender.getCodec().getName());
		msgKillAgent.setOntology(ontology.getName());
		
		KillAgent killAgent = new KillAgent(agentAID);

		Action action = new Action(agentSender.getAID(), killAgent);
		
		try {
			agentSender.getContentManager().fillContent(msgKillAgent, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " + KillAgent.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " + KillAgent.class.getSimpleName(), e);
		}

		ACLMessage msgReturned = null;
		try {
			msgReturned = FIPAService
					.doFipaRequestClient(agentSender, msgKillAgent);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to " + KillAgent.class.getSimpleName(), e);
			return false;
		}

		String msgText = msgReturned.getContent();

		if (msgReturned.getPerformative() == ACLMessage.INFORM) {
			if (msgText.equals("OK")) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get AID of Agent ManagerAgent for Agent slave AID
	 * @param agent
	 * @param agentSlaveAID
	 * @return
	 */
	public static AID getManagerAgentOfAID(Agent_DistributedEA agent, AID agentSlaveAID) {

		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_DistributedEA.class.getSimpleName() + " can't be null");
		}
		if (agentSlaveAID == null) {
			throw new IllegalArgumentException("Argument " +
					AID.class.getSimpleName() + " can't be null");
		}
		
		AID [] aidManagerAgents = agent.searchDF(
				Agent_ManagerAgent.class.getName());
		
		String agentToSearchName = agentSlaveAID.getName();
		int index = agentToSearchName.lastIndexOf(
				Configuration.CONTAINER_NUMBER_PREFIX);
		String containerID = agentToSearchName.substring(
				index, agentToSearchName.length());
		
		for (AID managerI: aidManagerAgents) {
			
			String managerNameI = managerI.getName();
			if (managerNameI.endsWith(containerID)) {
				return managerI;
			}
		}
		
		return null;
	}
	
	/**
	 * Send request {@link KillContainer} to all {@link Agent_ManagerAgent}s.
	 * @param agentSender
	 * @param logger
	 */
	public static void killAllContainers(Agent_DistributedEA agentSender,
			IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_DistributedEA.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}

		killAllComputingAgent(agentSender, logger);
		
		AID [] aidManagerAgents = agentSender.searchDF(
				Agent_ManagerAgent.class.getName());
		
		for (AID managerAidI : aidManagerAgents) {	

			sendKillContainer(agentSender, managerAidI, logger);
		}
	}
	
	/**
	 * Send request to kill container
	 * 
	 * @param agentSender
	 * @param agentReciever
	 * @return
	 */
	public static boolean sendKillContainer(Agent_DistributedEA agentSender,
			AID agentReciever, IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_DistributedEA.class.getSimpleName() + " can't be null");
		}
		if (agentReciever == null) {
			throw new IllegalArgumentException("Argument " +
					AID.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgKillContainer = new ACLMessage(ACLMessage.REQUEST);
		msgKillContainer.addReceiver(agentReciever);
		msgKillContainer.setSender(agentSender.getAID());
		msgKillContainer.setLanguage(agentSender.getCodec().getName());
		msgKillContainer.setOntology(ontology.getName());

		KillContainer killContainer = new KillContainer();

		Action action = new Action(agentSender.getAID(), killContainer);
		
		try {
			agentSender.getContentManager().fillContent(msgKillContainer, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " + KillContainer.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " + KillContainer.class.getSimpleName(), e);
		}

		ACLMessage msgReturned = null;
		try {
			msgReturned = FIPAService
					.doFipaRequestClient(agentSender, msgKillContainer);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to " + KillContainer.class.getSimpleName(), e);
			return false;
		}

		String msgText = msgReturned.getContent();

		if (msgReturned.getPerformative() == ACLMessage.INFORM) {
			if (msgText.equals("OK")) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns number of CPU available in all containers
	 * @param centramManager
	 * @param logger
	 * @return
	 */
	public int getNumberOfAvailableCPU(Agent_CentralManager agentSender,
			AgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		AID [] aidManagerAgents = agentSender.searchDF(
				Agent_ManagerAgent.class.getName());
		
		int numberOfCPU = 0;
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.requestForNodeInfo(
					agentSender, managerAidI, logger);
			
			numberOfCPU += nodeInfoI.getTotalCPUNumber();
		}
		
		return numberOfCPU;
	}
	
}
