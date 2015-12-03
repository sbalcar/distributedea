package org.distributedea.agents.systemagents.datamanager;

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.results.PartResult;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class DataManagerService {

	/**
	 * Sends PartResult to DataManager
	 * 
	 * @param agent
	 * @param result
	 * @param logger
	 */
	public static void sendPartResultMessage(Agent_DistributedEA agent,
			PartResult result, AgentLogger logger) {
		
		AID dataManagerAID = new AID(AgentNames.DATA_MANAGER.getName(), false);

		Ontology ontology = ResultOntology.getInstance();
		
	     ACLMessage msgPartResult = new ACLMessage(ACLMessage.INFORM);
	     msgPartResult.addReceiver(dataManagerAID);
	     msgPartResult.setSender(agent.getAID());
	     msgPartResult.setLanguage(agent.getCodec().getName());
	     msgPartResult.setOntology(ontology.getName());
	     
	     Action action = new Action(agent.getAID(), result);

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
