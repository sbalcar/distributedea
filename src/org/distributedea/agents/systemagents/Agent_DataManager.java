package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.logging.FileLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.data.SaveTheBestIndividual;
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
				
				Serializable content = null;
				try {
					
					content = request.getContentObject();
					
					getLogger().log(Level.INFO, "Request for " +
							content.getClass().getSimpleName());
					
					if (content instanceof SaveTheBestIndividual) {
						SaveTheBestIndividual saveResultOfComputing =
								(SaveTheBestIndividual)content;
						return respondToSaveTheBestIndividual(request, saveResultOfComputing);
					
					} else if (content instanceof SaveResultOfIteration) {
						SaveResultOfIteration saveResult =
								(SaveResultOfIteration)content;
						return respondToSaveResults(request, saveResult);
					}

				} catch (UnreadableException e) {
					getLogger().logThrowable("Problem extracting content", e);
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
			SaveTheBestIndividual saveResultOfComputing) {
		if (saveResultOfComputing == null ||
				! saveResultOfComputing.valid(new TrashLogger())) {
			getLogger().log(Level.SEVERE, "Cann't log the best individual");
			return null;
		}
		
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

	protected ACLMessage respondToSaveResults(ACLMessage request, SaveResultOfIteration saveResult) {
		if (saveResult == null ||
				! saveResult.valid(new TrashLogger())) {
			getLogger().log(Level.SEVERE, "Cann't log the best individual");
			return null;
		}
		
		ResultOfIteration results = saveResult.getResults();
		
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
