package org.distributedea.agents.systemagents;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.distributedea.AgentNames;
import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.Agent_ComputingAgent;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.results.PartResult;

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
		
		// clears  old results of computing - rewrites old file
		cleanResult();

		// clear old logs of Computing Agents - removes old files
		cleanLogDirectory();
		
		
		MessageTemplate mesTemplate =
				MessageTemplate.MatchPerformative(ACLMessage.INFORM);

		addBehaviour(new AchieveREResponder(this, mesTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(request);

					if (action.getAction() instanceof PartResult) {
						return respondToPartResult(request, action);
					}


				} catch (OntologyException e) {
					logger.logThrowable("Problem extracting content", e);
				} catch (CodecException e) {
					logger.logThrowable("Codec problem", e);
				}

				return null;
			}

			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				return null;
			}
		});

	}

	protected void registrDF() {
        
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(getType());
        sd.setName(AgentNames.DATA_MANAGER.getName());
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);
        
        try {  
            DFService.register(this, dfd );  
        
        } catch (FIPAException fe) {
        	logger.logThrowable("Registration faild", fe);
        }
	}
	
	private Map<String, PartResult> partResultOfCA = new HashMap<String, PartResult>();
	
	protected ACLMessage respondToPartResult(ACLMessage request, Action action) {
		
		PartResult result = (PartResult)action.getAction();
		AID aid = request.getSender();
		String senderName = aid.getName();

		partResultOfCA.put(senderName, result);
		
		AID [] aidComputingAgents = this.searchDF(
				Agent_ComputingAgent.class.getName());
		int numberOfCA = aidComputingAgents.length;
		//System.out.println("numberOfCA: " + numberOfCA);
		
		if (partResultOfCA.size() == numberOfCA) {
		
			String row = "";
			for (PartResult partResultI : partResultOfCA.values()) {
				row += partResultI.getFitnessResult() + " ";
			}
			
			String fileName = Configuration.getResultFile();
			try {
				Writer writer = new BufferedWriter(new FileWriter(fileName, true));
				writer.append(row + "\n");
				writer.close();
			} catch (IOException e) {
				logger.logThrowable("Part result can't be logged", e);
			}
		}
		
		return null;
	}

	private void cleanLogDirectory() {
		
		String logDirectoryName = Configuration.getComputingAgentLogDirectory();
		File logDirectory = new File(logDirectoryName);
		
		File[] files = logDirectory.listFiles();
	    if (files != null) { //some JVMs return null for empty dirs
	        for(File fileI: files) {
	            if(! fileI.isDirectory()) {
	            	fileI.delete();
	            }
	        }
	    }
	}
	
	private void cleanResult() {
		
		String fileName = Configuration.getResultFile();
		Writer writer;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.close();
		} catch (IOException e) {
			logger.logThrowable("Error by rewiting file", e);
		}
	}
	
}
