package org.distributedea.agents.systemagents.centralloger;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.logger.LogMessage;

public class CentralLogerService {

	/**
	 * Send request to log message
	 * 
	 * @param agent
	 * @param message
	 * @return
	 */
	public boolean sendLogMessage(Agent_DistributedEA agent, String message) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agentKiller can't be null");
		}

		AID agentCentralLogerAID = new AID(AgentNames.CENTRAL_LOGER.getName(), false);
		Ontology ontology = LogOntology.getInstance();

		ACLMessage msgLog = new ACLMessage(ACLMessage.REQUEST);
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
			agent.logException("CodecException by sending LogMessage", e);
		} catch (OntologyException e) {
			agent.logException("OntologyException by sending LogMessage", e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgLog);
		} catch (FIPAException e) {
			agent.logException("FIPAException by sending LogMessage", e);
			return false;
		}
		
		if (msgRetursName.getPerformative() == ACLMessage.AGREE) {
			return true;
		}
		
		return false;
	}
	
}
