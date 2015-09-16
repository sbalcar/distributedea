package org.distributedea.agents.computingagents.computingagent;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.problem.Problem;

public class ComputingAgentService {

	public static boolean sendStartComputing(Agent_DistributedEA agent, AID computingAgent,
			Problem problem, AgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agentKiller can't be null");
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
			logger.logThrowable("CodecException by sending LogMessage", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending LogMessage", e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgStartComputing);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by sending LogMessage", e);
			return false;
		}
		
		if (msgRetursName.getPerformative() == ACLMessage.AGREE) {
			return true;
		}
		
		return false;
	}
}
