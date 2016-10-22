package org.distributedea.agents.systemagents;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.content.Concept;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.basic.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.logger.LogMessage;

/**
 * Agent represents central Logger
 * @author stepan
 *
 */
public class Agent_CentralLogger extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	/**
     * Returns list of all ontologies
     */
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		
		return ontologies;
	}

	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new FileLogger(this);
		}
		return logger;
	}
	
	@Override
	protected void setup() {
		
		initAgent();
		registrDF();

		MessageTemplate mesTemplate =
				MessageTemplate.MatchPerformative(ACLMessage.INFORM);

		addBehaviour(new AchieveREResponder(this, mesTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(request);

					Concept concept = action.getAction();
					getLogger().log(Level.INFO, "Inform with " +
							concept.getClass().getSimpleName());
					
					if (concept instanceof LogMessage) {
						return respondToLogMessage(request, action);
					}


				} catch (OntologyException e) {
					getLogger().logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					getLogger().logThrowable("Codec problem", e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				return failure;
			}
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
				return null;
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
		
		LogMessage logMessage = (LogMessage) a.getAction();		
		String message = logMessage.getMessage();
		getLogger().log(Level.INFO, message);
		
		return null;
	}
	
}
