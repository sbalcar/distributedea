package org.distributedea.agents.systemagents.monitor;

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

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.MonitorOntology;
import org.distributedea.ontology.monitor.GetStatistic;
import org.distributedea.ontology.monitor.Statistic;

public class MonitorService {

	public static Statistic sendGetStatistic(Agent_DistributedEA agent,
			IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}

		AID monitorAID = new AID(AgentNames.MONITOR.getName(), false);
		
		Ontology ontology = MonitorOntology.getInstance();

		ACLMessage msgGetStatistic = new ACLMessage(ACLMessage.REQUEST);
		msgGetStatistic.addReceiver(monitorAID);
		msgGetStatistic.setSender(agent.getAID());
		msgGetStatistic.setLanguage(agent.getCodec().getName());
		msgGetStatistic.setOntology(ontology.getName());

		
		GetStatistic getStatistic = new GetStatistic();
		
		Action action = new Action(agent.getAID(), getStatistic);
		
		try {
			agent.getContentManager().fillContent(msgGetStatistic, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending GetStatistis", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending GetStatistis", e);
		}
		
		
		ACLMessage msgMethodDescription = null;
		try {
			msgMethodDescription = FIPAService
					.doFipaRequestClient(agent, msgGetStatistic);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving MethodDescription", e);
			return null;
		}
		
		Statistic statistic = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgMethodDescription);

			statistic = (Statistic) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving Statistic", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving Statistic", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving Statistic", e);
		}
		
		return statistic;

	}
}
