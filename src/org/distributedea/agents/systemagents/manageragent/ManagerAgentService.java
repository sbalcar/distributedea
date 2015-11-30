package org.distributedea.agents.systemagents.manageragent;

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
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.management.CreateAgent;
import org.distributedea.ontology.management.CreatedAgent;
import org.distributedea.ontology.management.KillAgent;
import org.distributedea.ontology.management.KillContainer;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.agent.Arguments;
import org.distributedea.ontology.management.computingnode.DescribeNode;
import org.distributedea.ontology.management.computingnode.NodeInfo;

/**
 * Wrapper for all services which provides agent Manager
 * @author stepan
 *
 */
public class ManagerAgentService {

	/**
	 * Sends request to information about node
	 * 
	 * @param agentSender
	 * @param agentReciever
	 * @param logger
	 * @return
	 */
	public static NodeInfo requestForNodeInfo(Agent_DistributedEA agentSender,
			AID agentReciever, AgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException(
					"Argument agentSender can't be null");
		}

		if (agentReciever == null) {
			throw new IllegalArgumentException(
					"Argument agentReciever can't be null");
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
			logger.logThrowable("CodecException by sending DescribeNode", e);
			return null;
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending DescribeNode", e);
			return null;
		}

		ACLMessage nodeInfoDescription = null;
		try {
			nodeInfoDescription = FIPAService
					.doFipaRequestClient(agentSender, msgDescribeNode);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to DescribeNode", e);
			return null;
		}
		
		NodeInfo nodeInfo = null;
		try {
			Result result = (Result) agentSender.getContentManager()
					.extractContent(nodeInfoDescription);

			nodeInfo = (NodeInfo) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving NodeInfo", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving NodeInfo", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving NodeInfo", e);
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
	public static AID sendCreateAgent(Agent_DistributedEA agentSender,
			AID agentReciever, String agentType, String agentName, List<Argument> arguments,
			AgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException(
					"Argument agentSender can't be null");
		}

		if (agentReciever == null) {
			throw new IllegalArgumentException(
					"Argument agentReciever can't be null");
		}
		
		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgCreateA = new ACLMessage(ACLMessage.REQUEST);
		msgCreateA.addReceiver(agentReciever);
		msgCreateA.setSender(agentSender.getAID());
		msgCreateA.setLanguage(agentSender.getCodec().getName());
		msgCreateA.setOntology(ontology.getName());

		CreateAgent createAgent = new CreateAgent();
		createAgent.setType(agentType);
		createAgent.setName(agentName);
		createAgent.setArguments(new Arguments(arguments));
		
		Action action = new Action(agentSender.getAID(), createAgent);
		
		try {
			agentSender.getContentManager().fillContent(msgCreateA, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending CreateAgent", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending CreateAgent", e);
		}

		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agentSender, msgCreateA);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to CreateAgent", e);
			return null;
		}

		CreatedAgent createdAgent = null;
		try {
			Result result = (Result) agentSender.getContentManager()
					.extractContent(msgRetursName);

			createdAgent = (CreatedAgent) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving CreatedAgent", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving CreatedAgent", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving CreatedAgent", e);
		}
		
		return createdAgent.exportCreatedAgentName();
		
	}
	
	
	/**
	 * Sends request to kill agent
	 * 
	 * @param agentSender
	 * @param logger
	 * @return
	 */
	public static boolean sendKillAgent(Agent_DistributedEA agentSender, AID agentAID,
			AgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException(
					"Argument agentSender can't be null");
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
		
		KillAgent killAgent = new KillAgent();
		// local names are unique
		killAgent.setAgentName(agentAID.getLocalName());

		Action action = new Action(agentSender.getAID(), killAgent);
		
		try {
			agentSender.getContentManager().fillContent(msgKillAgent, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending KillAgent", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending KillAgent", e);
		}

		ACLMessage msgReturned = null;
		try {
			msgReturned = FIPAService
					.doFipaRequestClient(agentSender, msgKillAgent);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to KillAgent", e);
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
	 * Send request to kill container
	 * 
	 * @param agentSender
	 * @param agentReciever
	 * @return
	 */
	public static boolean sendKillContainer(Agent_DistributedEA agentSender,
			AID agentReciever, AgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException(
					"Argument agentKiller can't be null");
		}

		if (agentReciever == null) {
			throw new IllegalArgumentException(
					"Argument agentReciever can't be null");
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
			logger.logThrowable("CodecException by sending KillContainer", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending KillContainer", e);
		}

		ACLMessage msgReturned = null;
		try {
			msgReturned = FIPAService
					.doFipaRequestClient(agentSender, msgKillContainer);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving the answer to KillContainer", e);
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
	public int getNumberOfAvailableCPU(Agent_CentralManager centramManager,
			AgentLogger logger) {
		
		AID [] aidManagerAgents = centramManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		int numberOfCPU = 0;
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.requestForNodeInfo(
					centramManager, managerAidI, logger);
			
			numberOfCPU += nodeInfoI.getTotalCPUNumber();
		}
		
		return numberOfCPU;
	}
	
}
