package org.distributedea.agents.systemagents;

import jade.content.Concept;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.individuals.SaveTheBestIndividual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.SaveResultOfIteration;


/**
 * System-agent who manages data resources multi-agent framework
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

	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new FileLogger(this);
		}
		return logger;
	}
	
	protected void initAgent() {
		
		//cleans result dir
		try {
			FilesystemInitTool.clearResultDir(getLogger());
		} catch (Exception e1) {
			getLogger().logThrowable("Can not clear result directory", e1);
		}
		
		super.initAgent();
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

					Concept concept = action.getAction();
					getLogger().log(Level.INFO, "Request for " +
							concept.getClass().getSimpleName());
					
					if (concept instanceof SaveTheBestIndividual) {
						return respondToSaveTheBestIndividual(request, action);
					
					} else if (concept instanceof SaveResultOfIteration) {
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
	

	protected ACLMessage respondToSaveTheBestIndividual(ACLMessage request,
			Action action) {
		
		SaveTheBestIndividual saveResultOfComputing = (SaveTheBestIndividual)action.getAction();
		
		IndividualWrapper result = saveResultOfComputing.getResult();
		
		String fileName = FileNames.getResultFile(result.getJobID());
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
		
		SaveResultOfIteration saveResult = (SaveResultOfIteration)action.getAction();
		ResultOfIteration results = saveResult.getResults();
		
		if (results == null || ! results.valid(getLogger())) {
			getLogger().log(Level.WARNING, "Received " +
					SaveResultOfIteration.class.getSimpleName() + " is not valid");
		}
		
		String monitoringDirName = FileNames.
				getResultDirectoryMonitoringDirectory(results.getJobID());
		
		File dir = new File(monitoringDirName);
		if (! dir.isDirectory()) {
			dir.mkdir();
		}
		
		try {
			results.exportXML(new File(monitoringDirName));
		} catch (Exception e) {
			getLogger().logThrowable("", e);
		}
		
		return null;
	}
	
}
