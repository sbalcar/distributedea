package org.distributedea.agents.systemagents;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.computing.result.ResultOfComputing;

/**
 * System-agent who manage data resources multi-agent framework
 * @author stepan
 *
 */
public class Agent_DataManager extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(ResultOntology.getInstance());
		
		return ontologies;
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

					if (action.getAction() instanceof ResultOfComputing) {
						return respondToResultOfComputing(request, action);
					}


				} catch (OntologyException e) {
					getLogger().logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					getLogger().logThrowable("Codec problem", e);
				}

				return null;
			}

			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}
		});

	}
	
	protected ACLMessage respondToResultOfComputing(ACLMessage request,
			Action action) {
		
		ResultOfComputing result = (ResultOfComputing)action.getAction();
		
		String fileName = Configuration.getResultFile(result.getJobID());
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.append(result.getFitnessValue() + "\n");
			writer.close();
		} catch (IOException e) {
			getLogger().logThrowable("Part result can't be logged", e);
		}
		
		return null;
	}	

	
}
