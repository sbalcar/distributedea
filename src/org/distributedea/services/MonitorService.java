package org.distributedea.services;

import javax.management.monitor.Monitor;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.MonitorOntology;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.monitor.GetStatistic;
import org.distributedea.ontology.monitor.StartMonitoring;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.problem.IProblem;

/**
 * Creates the structure of services offering all descendants
 * of {@link Monitor} 
 * @author stepan
 *
 */
public class MonitorService {

	/**
	 * Sends {@link StartMonitoring}.
	 * @param agent
	 * @param jobID
	 * @param problemToSolveClass
	 * @param logger
	 */
	public static void startsMonitoring(Agent_DistributedEA agent,
			JobID jobID, IProblem problemToSolve,
			MethodDescriptions agentsToMonitor, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_DistributedEA.class.getSimpleName() + " agent can't be null");
		}
		if (jobID == null || ! jobID.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		if (problemToSolve == null || ! problemToSolve.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		if (agentsToMonitor == null || ! agentsToMonitor.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		
		AID monitorAID = new AID(AgentNames.MONITOR.getName(), false);
		
		Ontology ontology = MonitorOntology.getInstance();

		ACLMessage msgStartStatistic = new ACLMessage(ACLMessage.REQUEST);
		msgStartStatistic.addReceiver(monitorAID);
		msgStartStatistic.setSender(agent.getAID());
		msgStartStatistic.setLanguage(agent.getCodec().getName());
		msgStartStatistic.setOntology(ontology.getName());

		
		StartMonitoring startMonitoring = new StartMonitoring(jobID,
				problemToSolve, agentsToMonitor);
		
		Action action = new Action(agent.getAID(), startMonitoring);
		
		try {
			agent.getContentManager().fillContent(msgStartStatistic, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					StartMonitoring.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					StartMonitoring.class.getSimpleName(), e);
		}
		
		agent.send(msgStartStatistic);
	}
	
	/**
	 * Sends request {@link GetStatistic} for current statistical results
	 * of computing. Specified with {@link JobID}
	 * @param agent
	 * @param jobID
	 * @param logger
	 * @return
	 */
	public static Statistic getStatistic(Agent_DistributedEA agent,
			JobID jobID, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_DistributedEA.class.getSimpleName() + " agent can't be null");
		}
		if (jobID == null || ! jobID.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		
		
		AID monitorAID = new AID(AgentNames.MONITOR.getName(), false);
		
		Ontology ontology = MonitorOntology.getInstance();

		ACLMessage msgGetStatistic = new ACLMessage(ACLMessage.REQUEST);
		msgGetStatistic.addReceiver(monitorAID);
		msgGetStatistic.setSender(agent.getAID());
		msgGetStatistic.setLanguage(agent.getCodec().getName());
		msgGetStatistic.setOntology(ontology.getName());

		
		GetStatistic getStatistic = new GetStatistic(jobID);
		if (! getStatistic.valid(logger)) {
			throw new IllegalStateException();
		}
		
		Action action = new Action(agent.getAID(), getStatistic);
		
		try {
			agent.getContentManager().fillContent(msgGetStatistic, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					GetStatistic.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					GetStatistic.class.getSimpleName(), e);
		}
		
		
		ACLMessage msgStatistic = null;
		try {
			msgStatistic = FIPAService
					.doFipaRequestClient(agent, msgGetStatistic);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving " +
					Statistic.class.getSimpleName(), e);
			return null;
		}
		
		// send as object
		Statistic statistic = null;
		try {
			statistic = (Statistic) msgStatistic.getContentObject();
		} catch (UnreadableException e) {
			logger.logThrowable("UnreadableException by receiving " +
					Statistic.class.getSimpleName(), e);
		}
		
		return statistic;
	}
	
	/**
	 * Sends request {@link AccessesResult} for currently
	 * the best {@link IndividualWrapper} of computing.
	 * @param agent
	 * @param logger
	 * @return
	 */
	public static IndividualWrapper getBestIndividual(Agent_CentralManager agent,
			IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_DistributedEA.class.getSimpleName() + " agent can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		
		
		AID monitorAID = new AID(AgentNames.MONITOR.getName(), false);
		
		Ontology ontology = MonitorOntology.getInstance();

		ACLMessage msgAccessesResult = new ACLMessage(ACLMessage.REQUEST);
		msgAccessesResult.addReceiver(monitorAID);
		msgAccessesResult.setSender(agent.getAID());
		msgAccessesResult.setLanguage(agent.getCodec().getName());
		msgAccessesResult.setOntology(ontology.getName());

		
		AccessesResult accessesResult = new AccessesResult();
		
		Action action = new Action(agent.getAID(), accessesResult);
		
		try {
			agent.getContentManager().fillContent(msgAccessesResult, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					AccessesResult.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					AccessesResult.class.getSimpleName(), e);
		}
		
		
		ACLMessage msgIndivWrp = null;
		try {
			msgIndivWrp = FIPAService
					.doFipaRequestClient(agent, msgAccessesResult);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving " +
					IndividualWrapper.class.getSimpleName(), e);
			return null;
		}
		
		// send as object
		IndividualWrapper indivWrp = null;
		try {
			indivWrp = (IndividualWrapper) msgIndivWrp.getContentObject();
		} catch (UnreadableException e) {
			logger.logThrowable("UnreadableException by receiving " +
					IndividualWrapper.class.getSimpleName(), e);
		}
		
		return indivWrp;
	}
}
