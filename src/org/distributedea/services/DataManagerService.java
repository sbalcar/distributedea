package org.distributedea.services;

import org.distributedea.AgentNames;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_DataManager;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.individuals.SaveTheBestIndividual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.SaveResultOfIteration;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

/**
 * Wrapper for all services which provides {@link Agent_DataManager}
 * @author stepan
 *
 */
public class DataManagerService {

	/**
	 * Ensures to save {@link IndividualWrapper} as best result of given {@link Iteration}.
	 * Communication is formed by sending request {@link SaveTheBestIndividual}.
	 * @param agent
	 * @param iteration
	 * @param individualWrp
	 * @param logger
	 */
	public static void saveIndividualAsBestSolutionOfIteration(
			Agent_CentralManager agent, Iteration iteration,
			IndividualWrapper individualWrp, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() +
					" can not be null");
		}
		if (individualWrp == null || ! individualWrp.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " is not valid");
		}
		if (iteration == null || ! iteration.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can not be null");
		}
		
		AID dataManagerAID = new AID(AgentNames.DATA_MANAGER.getName(), false);
		
		Ontology ontology = ResultOntology.getInstance();
		
		ACLMessage msgPartResult = new ACLMessage(ACLMessage.REQUEST);
		msgPartResult.addReceiver(dataManagerAID);
		msgPartResult.setSender(agent.getAID());
		msgPartResult.setLanguage(agent.getCodec().getName());
		msgPartResult.setOntology(ontology.getName());
	    
		SaveTheBestIndividual save = new SaveTheBestIndividual(iteration, individualWrp);
		
		Action action = new Action(agent.getAID(), save);
		
		try {
			agent.getContentManager().fillContent(msgPartResult, action);
		} catch (CodecException e) {
			logger.logThrowable("Can not send " + SaveTheBestIndividual.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("Can not send " + SaveTheBestIndividual.class.getSimpleName(), e);
		}

	    agent.send(msgPartResult);
	}

	/**
	 * Ensures to save {@link ResultOfIteration} as computation statistic of
	 * given {@link Iteration}. Communication is formed by sending request
	 * {@link SaveResultOfIteration}.
	 * @param agent
	 * @param iteration
	 * @param result
	 * @param logger
	 */
	public static void saveResultOfIteration(Agent_CentralManager agent,
			Iteration iteration, ResultOfIteration result, IAgentLogger logger) {

		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() +
					" can not be null");
		}
		if (result == null || ! result.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					ResultOfIteration.class.getSimpleName() + " is not valid");
		}
		if (iteration == null || ! iteration.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can not be null");
		}

		AID dataManagerAID = new AID(AgentNames.DATA_MANAGER.getName(), false);
		
		Ontology ontology = ResultOntology.getInstance();
		
		ACLMessage msgPartResult = new ACLMessage(ACLMessage.REQUEST);
		msgPartResult.addReceiver(dataManagerAID);
		msgPartResult.setSender(agent.getAID());
		msgPartResult.setLanguage(agent.getCodec().getName());
		msgPartResult.setOntology(ontology.getName());
	    
		SaveResultOfIteration save = new SaveResultOfIteration(result);
		
		Action action = new Action(agent.getAID(), save);
		
		try {
			agent.getContentManager().fillContent(msgPartResult, action);
		} catch (CodecException e) {
			logger.logThrowable("Can not send " + SaveResultOfIteration.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("Can not send " + SaveResultOfIteration.class.getSimpleName(), e);
		}

	    agent.send(msgPartResult);
	}

}