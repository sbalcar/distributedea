package org.distributedea.tests.throughputcapacity;

import java.io.IOException;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;

public class ReceiverService {

	/**
	 * Sends {@link IndividualEvaluated} to agent {@link Agent_Receiver}
	 * @param agent
	 * @param individual
	 * @param logger
	 */
	public static void sendAsObjectIndividualToReceiver(Agent_DistributedEA agent,
			IndividualsWrappers individuals, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (individuals == null || ! individuals.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IndividualsWrappers.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		
		AID receiverAID = new AID("Receiver", false);
		
		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgIndividual = new ACLMessage(ACLMessage.INFORM);
		msgIndividual.addReceiver(receiverAID);
		msgIndividual.setSender(agent.getAID());
		msgIndividual.setLanguage(agent.getCodec().getName());
		msgIndividual.setOntology(ontology.getName());

		// sends individual as object
		try {
			msgIndividual.setContentObject(individuals);
		} catch (IOException e) {
			logger.logThrowable("IOException by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		}
		
		agent.send(msgIndividual);
	}
	
	
	public static void sendAsOntologyIndividualToReceiver(Agent_DistributedEA agent,
			IndividualsWrappers individuals, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (individuals == null || ! individuals.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IndividualsWrappers.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		
		AID receiverAID = new AID("Receiver", false);
		
		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgIndividual = new ACLMessage(ACLMessage.INFORM_IF);
		msgIndividual.addReceiver(receiverAID);
		msgIndividual.setSender(agent.getAID());
		msgIndividual.setLanguage(agent.getCodec().getName());
		msgIndividual.setOntology(ontology.getName());
		
		Action action = new Action(receiverAID, individuals);
		
		try {
			agent.getContentManager().fillContent(msgIndividual, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		}
		
		agent.send(msgIndividual);
	}
}
