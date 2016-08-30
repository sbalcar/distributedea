package org.distributedea.services;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.Agent_CentralLoger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.logger.LogMessage;

public class CentralLogerService {

	/**
	 * Ensures to save log message by using {@link Agent_CentralLoger}.
	 * Agent sends request {@link LogMessage} to log message and receives
	 * the agree-message.
	 * 
	 * @param agent
	 * @param message
	 * @return
	 */
	public static void logMessage(Agent_DistributedEA agent, String message, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_DistributedEA.class.getSimpleName() + " can't be null");
		}
		if (message == null) {
			throw new IllegalArgumentException("Argument " +
					String.class.getSimpleName() + " can't be null");
		}

		AID agentCentralLogerAID = new AID(AgentNames.CENTRAL_LOGER.getName(), false);
		Ontology ontology = LogOntology.getInstance();

		ACLMessage msgLog = new ACLMessage(ACLMessage.INFORM);
		msgLog.addReceiver(agentCentralLogerAID);
		msgLog.setSender(agent.getAID());
		msgLog.setLanguage(agent.getCodec().getName());
		msgLog.setOntology(ontology.getName());

		LogMessage logMessage = new LogMessage();
		logMessage.setMessage(message);
		
		Action action = new Action(agent.getAID(), logMessage);
		
		try {
			agent.getContentManager().fillContent(msgLog, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					LogMessage.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					LogMessage.class.getSimpleName(), e);
		}
		
		agent.send(msgLog);
	}
	
}
