package org.distributedea.agents.computingagents.computingagent;

import java.util.ArrayList;
import java.util.Arrays;
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

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.Agent_ComputingAgent;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.problem.Problem;

public class ComputingAgentService {

	public static boolean sendStartComputing(Agent_DistributedEA agent, AID computingAgent,
			Problem problem, AgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}

		Ontology ontology = ComputingOntology.getInstance();

		ACLMessage msgStartComputing = new ACLMessage(ACLMessage.REQUEST);
		msgStartComputing.addReceiver(computingAgent);
		msgStartComputing.setSender(agent.getAID());
		msgStartComputing.setLanguage(agent.getCodec().getName());
		msgStartComputing.setOntology(ontology.getName());

		StartComputing startComputing = new StartComputing();
		startComputing.setProblem(problem);
		
		Action action = new Action(agent.getAID(), startComputing);
		
		try {
			agent.getContentManager().fillContent(msgStartComputing, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending StartComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending StartComputing", e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgStartComputing);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving StartComputing answer", e);
			return false;
		}
		
		if (msgRetursName.getPerformative() == ACLMessage.AGREE) {
			return true;
		}
		
		return false;
	}
	
	
	public static boolean sendPrepareYourselfToKill(Agent_DistributedEA agent, AID computingAgent,
			AgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}

		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgStartComputing = new ACLMessage(ACLMessage.REQUEST);
		msgStartComputing.addReceiver(computingAgent);
		msgStartComputing.setSender(agent.getAID());
		msgStartComputing.setLanguage(agent.getCodec().getName());
		msgStartComputing.setOntology(ontology.getName());

		PrepareYourselfToKill prepareToKill = new PrepareYourselfToKill();
		
		Action action = new Action(agent.getAID(), prepareToKill);
		
		try {
			agent.getContentManager().fillContent(msgStartComputing, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending PrepareYourselfToKill", e);
			return false;
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending PrepareYourselfToKill", e);
			return false;
		}
	
		agent.send(msgStartComputing);
		return true;
	}
	
	public static ResultOfComputing sendAccessesResult(Agent_DistributedEA agent,
			AID computingAgent, AgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agentKiller can't be null");
		}

		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgAccessesResult = new ACLMessage(ACLMessage.REQUEST);
		msgAccessesResult.addReceiver(computingAgent);
		msgAccessesResult.setSender(agent.getAID());
		msgAccessesResult.setLanguage(agent.getCodec().getName());
		msgAccessesResult.setOntology(ontology.getName());

		AccessesResult accessesResult = new AccessesResult();
		
		Action action = new Action(agent.getAID(), accessesResult);
		
		try {
			agent.getContentManager().fillContent(msgAccessesResult, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending AccessesResult", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending AccessesResult", e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgAccessesResult);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving ResultOfComputing", e);
			return null;
		}
		
		ResultOfComputing resultOfComputing = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgRetursName);

			resultOfComputing = (ResultOfComputing) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving ResultOfComputing", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving ResultOfComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving ResultOfComputing", e);
		}
		
		return resultOfComputing;
	}
	
	
	public static void sendIndividualToNeighbours(Agent_DistributedEA agent,
			Individual individual, AgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent sender can't be null");
		}
		
		AID [] aidOfComputingAgents = agent.searchDF(
				Agent_ComputingAgent.class.getName());
		if (aidOfComputingAgents == null) {
			throw new IllegalStateException("Computing agent can't find any neighbour " + agent.getAID().getLocalName());
		}
		List<AID> aidOfComputingAgentsWithoutSender = new ArrayList<AID>(Arrays.asList(aidOfComputingAgents));
		aidOfComputingAgentsWithoutSender.remove(agent.getAID());
		
		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgIndividual = new ACLMessage(ACLMessage.INFORM);
		for (AID computingAgentI : aidOfComputingAgentsWithoutSender) {
			msgIndividual.addReceiver(computingAgentI);	
		}
		msgIndividual.setSender(agent.getAID());
		msgIndividual.setLanguage(agent.getCodec().getName());
		msgIndividual.setOntology(ontology.getName());

		
		Action action = new Action(agent.getAID(), individual);
		
		try {
			agent.getContentManager().fillContent(msgIndividual, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending Individual", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending Individual", e);
		}
		
		agent.send(msgIndividual);
	}
	
	
}
