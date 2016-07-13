package org.distributedea.agents.systemagents.datamanager;

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.individuals.SaveBestIndividual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.SaveResults;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class DataManagerService {

	public static void sendSaveBestIndividual(Agent_DistributedEA agent,
			Iteration iteration, IndividualWrapper result, IAgentLogger logger) {
		
		AID dataManagerAID = new AID(AgentNames.DATA_MANAGER.getName(), false);
		
		Ontology ontology = ResultOntology.getInstance();
		
		ACLMessage msgPartResult = new ACLMessage(ACLMessage.REQUEST);
		msgPartResult.addReceiver(dataManagerAID);
		msgPartResult.setSender(agent.getAID());
		msgPartResult.setLanguage(agent.getCodec().getName());
		msgPartResult.setOntology(ontology.getName());
	    
		SaveBestIndividual save = new SaveBestIndividual(iteration, result);
		
		Action action = new Action(agent.getAID(), save);
		
		try {
			agent.getContentManager().fillContent(msgPartResult, action);
		} catch (CodecException e) {
			logger.logThrowable("Codec Exception by sending", e);
		} catch (OntologyException e) {
			logger.logThrowable("Ontology Exception by sending", e);
		}

	    agent.send(msgPartResult);
	}

	public static void sendResultOfIteration(Agent_DistributedEA agent,
			Iteration iteration, ResultOfIteration result, IAgentLogger logger) {
		
		AID dataManagerAID = new AID(AgentNames.DATA_MANAGER.getName(), false);
		
		Ontology ontology = ResultOntology.getInstance();
		
		ACLMessage msgPartResult = new ACLMessage(ACLMessage.REQUEST);
		msgPartResult.addReceiver(dataManagerAID);
		msgPartResult.setSender(agent.getAID());
		msgPartResult.setLanguage(agent.getCodec().getName());
		msgPartResult.setOntology(ontology.getName());
	    
		SaveResults save = new SaveResults(result);
		
		Action action = new Action(agent.getAID(), save);
		
		try {
			agent.getContentManager().fillContent(msgPartResult, action);
		} catch (CodecException e) {
			logger.logThrowable("Codec Exception by sending", e);
		} catch (OntologyException e) {
			logger.logThrowable("Ontology Exception by sending", e);
		}

	    agent.send(msgPartResult);
	}

}
