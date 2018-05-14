package org.distributedea.tests.throughputcapacity;

import jade.content.Concept;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;


/**
 * Agent receiver to test the throughput capacity of Jade 
 * @author stepan
 *
 */
public class Agent_Receiver extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(ResultOntology.getInstance());
		return ontologies;
	}

	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new TrashLogger();
		}
		return logger;
	}

	@Override
	protected void setup() {
		
		initAgent();
		registrDF();
		
		
		
		final MessageTemplate mesTemplateResultInform =
				MessageTemplate.and(
						MessageTemplate.MatchOntology(ResultOntology.getInstance().getName()),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
		
		addBehaviour(new AchieveREResponder(this, mesTemplateResultInform) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgInform) {

				try {
					Serializable content =  msgInform.getContentObject();
					
					if (content instanceof IndividualsWrappers) {
						
						System.out.println("Receiver: Received object");
						IndividualsWrappers individualsWrps =
								(IndividualsWrappers) content;
						processIndividualsWrappers(msgInform, individualsWrps);
					}
					
				} catch (Exception e) {
					getLogger().logThrowable("Problem extracting content", e);
					System.out.println("Chyba");
				}

				return null;
			}
			
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}

		});
		
		final MessageTemplate mesTemplateResultFailure =
				MessageTemplate.and(
						MessageTemplate.MatchOntology(ResultOntology.getInstance().getName()),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF) );
		
		addBehaviour(new AchieveREResponder(this, mesTemplateResultFailure) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage msgInform) {

				try {
					Action action = (Action)
							getContentManager().extractContent(msgInform);

					Concept concept = action.getAction();
					
					if (concept instanceof IndividualsWrappers) {
						
						System.out.println("Receiver: Received ontology");
						IndividualsWrappers individualsWrps =
								(IndividualsWrappers) concept;
						processIndividualsWrappers(msgInform, individualsWrps);
					}
					
				} catch (Exception e) {
					getLogger().logThrowable("Problem extracting content", e);
					System.out.println("Chyba");
				}

				return null;
			}
			
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}

		});
		
	}

	
	protected void processIndividualsWrappers(ACLMessage msgInform,
			IndividualsWrappers individualsWrps) {
	
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		System.out.println("Receiver: " + dateFormat.format(cal.getTime()));

		IndividualWrapper indiv0 =
				individualsWrps.getIndividualsWrappers().get(0);
		String method = indiv0.getMethodDescription().toString();
		
		System.out.println(method);
	}
}
