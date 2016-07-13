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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.individuals.SaveBestIndividual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.SaveResults;


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
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mesTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(request);

					if (action.getAction() instanceof SaveBestIndividual) {
						return respondToResultOfComputing(request, action);
					
					} else if (action.getAction() instanceof SaveResults) {
						return respondToSaveResults(request, action);
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
		
		SaveBestIndividual saveResultOfComputing = (SaveBestIndividual)action.getAction();
		
		IndividualWrapper result = saveResultOfComputing.getResult();
		
		String fileName = Configuration.getResultFile(result.getJobID());
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.append(result.getIndividualEvaluated().getFitness() + "\n");
			writer.close();
		} catch (IOException e) {
			getLogger().logThrowable("Part result can't be logged", e);
		}
		
		return null;
	}	

	protected ACLMessage respondToSaveResults(ACLMessage request, Action action) {
		
		SaveResults saveResults = (SaveResults)action.getAction();
		ResultOfIteration results = saveResults.getResults();
		
		String monitoringDirName = Configuration.
				getResultDirectoryMonitoringDirectory(results.getJobID());
		
		try {
			results.exportXML(new File(monitoringDirName));
		} catch (FileNotFoundException | JAXBException e) {
			getLogger().logThrowable("", e);
		}
		
		return null;
	}
	
}
