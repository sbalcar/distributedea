package org.distributedea.agents.systemagents;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.logger.LogMessage;

public class Agent_CentralLoger extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	/**
     * Returns list of all ontologies that are used by CentralLoger agent.
     */
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		
		return ontologies;
	}

	@Override
	protected void setup() {
		
		initAgent();
		registrDF();

		MessageTemplate mesTemplate =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mesTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(request);

					/*
					 * Logging action
					 */
					if (action.getAction() instanceof LogMessage) {
						return respondToLogMessage(request, action);
					}


				} catch (OntologyException e) {
					logException("Problem extracting content", e);
				} catch (CodecException e) {
					logException("Codec problem", e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				return failure;
			}

		});

	}
	
	/**
	 * Responds to Log Messages
	 * 
	 * @param request
	 * @param a
	 * @return
	 */
	private ACLMessage respondToLogMessage(ACLMessage request, Action a) {

		logInfo("respondToLogMessage");

		@SuppressWarnings("unused")
		LogMessage logMessage = (LogMessage) a.getAction();


		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.AGREE);

		Result result = new Result(a, "OK");
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException("CodecException by sending the answer for LogMessage", e);
		} catch (OntologyException e) {
			logException("OntologyException by sending the answer for LogMessage", e);
		}

		return reply;
	}
	
	
	@Override
	public void logException(String string, Exception e) {
		ConsoleLogger.logThrowable(string + e.getMessage(), e);
	}

	@Override
	public void logSevere(String message) {
		ConsoleLogger.log(Level.SEVERE, message);
	}
	
	@Override
	public void logInfo(String text) {
		ConsoleLogger.log(Level.INFO, text);
	}
	
}
