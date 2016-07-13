package org.distributedea.agents.computingagents.computingagent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel.ResultsOfComputing;
import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.RequiredAgent;
import org.distributedea.ontology.helpmate.HelpmateList;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.management.EverythingPreparedToBeKilled;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.methoddescription.GetMethodDescription;
import org.distributedea.ontology.methoddescriptionwrapper.MethodDescriptionWrapper;
import org.distributedea.ontology.methoddescriptionwrapper.MethodDescriptionsWrapper;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class ComputingAgentService {

	
	public static MethodDescriptionsWrapper sendGetMethodDescriptions(Agent_DistributedEA centralManager,
			IAgentLogger logger) {
	
		// Get Result of Computing
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());

		MethodDescriptionsWrapper description = new MethodDescriptionsWrapper();
		for (AID computingAgentI : aidOfComputingAgents) {
			
			MethodDescriptionWrapper methodDescriptionI =
					sendGetMethodDescription(centralManager, computingAgentI, logger);
			
			description.addMethodDescriptionWrapper(methodDescriptionI);
		}

		return description;
	}
	
	/**
	 * Obtains MethodDescription from the specified computing Agent
	 * @param agent
	 * @param computingAgent
	 * @param problemStruct
	 * @param logger
	 * @return
	 */
	public static MethodDescriptionWrapper sendGetMethodDescription(Agent_DistributedEA agent, AID computingAgent,
			IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}

		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgGetMethodDescription = new ACLMessage(ACLMessage.REQUEST);
		msgGetMethodDescription.addReceiver(computingAgent);
		msgGetMethodDescription.setSender(agent.getAID());
		msgGetMethodDescription.setLanguage(agent.getCodec().getName());
		msgGetMethodDescription.setOntology(ontology.getName());

		
		GetMethodDescription description = new GetMethodDescription();
		
		Action action = new Action(agent.getAID(), description);
		
		try {
			agent.getContentManager().fillContent(msgGetMethodDescription, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending StartComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending StartComputing", e);
		}
		
		
		ACLMessage msgMethodDescription = null;
		try {
			msgMethodDescription = FIPAService
					.doFipaRequestClient(agent, msgGetMethodDescription);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving MethodDescription", e);
			return null;
		}
		
		MethodDescriptionWrapper methodDescription = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgMethodDescription);

			methodDescription = (MethodDescriptionWrapper) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving MethodDescription", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving MethodDescription", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving MethodDescription", e);
		}
		
		return methodDescription;

	}
	
	/**
	 * Obtains AgentConfiguration required for ManagerAgent
	 * @param agent
	 * @param computingAgent
	 * @param agentConfiguration
	 * @param logger
	 */
	public static void sendRequiredAgent(Agent_DistributedEA agent, AID computingAgent,
			AgentConfiguration agentConfiguration, IAgentLogger logger) {
		
		RequiredAgent requiredAgent = new RequiredAgent();
		requiredAgent.setAgentConfiguration(agentConfiguration);
		
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent sender can't be null");
		}
		
		Ontology ontology = ManagementOntology.getInstance();
		
		ACLMessage msgRequiredAgent = new ACLMessage(ACLMessage.INFORM);
		msgRequiredAgent.setSender(agent.getAID());
		msgRequiredAgent.addReceiver(computingAgent);
		msgRequiredAgent.setLanguage(agent.getCodec().getName());
		msgRequiredAgent.setOntology(ontology.getName());

		
		Action action = new Action(computingAgent, requiredAgent);
		
		try {
			agent.getContentManager().fillContent(msgRequiredAgent, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending Individual", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending Individual", e);
		}
		
		agent.send(msgRequiredAgent);
		
	}
	
	/**
	 * Sends message StartComputing contains Problem definition to Computing Agent
	 * @param agent
	 * @param computingAgent
	 * @param problemStruct
	 * @param logger
	 * @return
	 */
	public static boolean sendStartComputing(Agent_DistributedEA agent, AID computingAgent,
			ProblemStruct problemStruct, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}

		Ontology ontology = ComputingOntology.getInstance();

		ACLMessage msgStartComputing = new ACLMessage(ACLMessage.REQUEST);
		msgStartComputing.addReceiver(computingAgent);
		msgStartComputing.setSender(agent.getAID());
		msgStartComputing.setLanguage(agent.getCodec().getName());
		msgStartComputing.setOntology(ontology.getName());

		JobID jobID = problemStruct.getJobID();
		boolean individualDistribution = problemStruct.getIndividualDistribution();
		String problemFileName = problemStruct.getProblem().getProblemFileName();
		String problemTool = problemStruct.getProblemToolClass();
		
		ProblemWrapper wrapper = new ProblemWrapper();
		wrapper.setJobID(jobID);
		wrapper.setIndividualDistribution(individualDistribution);
		wrapper.setProblemFileName(problemFileName);
		wrapper.setProblemToolClass(problemTool);
		
		StartComputing startComputing = new StartComputing();
		startComputing.setProblemWrapper(wrapper);
		
		Action action = new Action(agent.getAID(), startComputing);
		
		try {
			agent.getContentManager().fillContent(msgStartComputing, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending StartComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending StartComputing", e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgStartComputing);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving StartComputing answer", e);
			return false;
		}
		
		if (msgRetursName.getPerformative() == ACLMessage.AGREE) {
			return true;
		}
		
		return false;
	}
	
	
	public static EverythingPreparedToBeKilled sendPrepareYourselfToKill(Agent_DistributedEA agent, AID computingAgent,
			IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}

		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgPrepareYourselfToKill = new ACLMessage(ACLMessage.REQUEST);
		msgPrepareYourselfToKill.addReceiver(computingAgent);
		msgPrepareYourselfToKill.setSender(agent.getAID());
		msgPrepareYourselfToKill.setLanguage(agent.getCodec().getName());
		msgPrepareYourselfToKill.setOntology(ontology.getName());

		PrepareYourselfToKill prepareToKill = new PrepareYourselfToKill();
		
		Action action = new Action(agent.getAID(), prepareToKill);
		
		try {
			agent.getContentManager().fillContent(msgPrepareYourselfToKill, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending PrepareYourselfToKill", e);
			return null;
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending PrepareYourselfToKill", e);
			return null;
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgPrepareYourselfToKill);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving ResultOfComputing", e);
			return null;
		}
		
		EverythingPreparedToBeKilled everythingPreparedToBeKilled = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgRetursName);

			everythingPreparedToBeKilled = (EverythingPreparedToBeKilled) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving ResultOfComputing", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving ResultOfComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving ResultOfComputing", e);
		}
		
		return everythingPreparedToBeKilled;
	}
	
	/**
	 * Obtains the current results from all computing Agents
	 * @param centralManager
	 * @param logger
	 * @return
	 */
	public static List<IndividualWrapper> sendAccessesResult(Agent_DistributedEA centralManager,
			IAgentLogger logger) {
		
		// Get Result of Computing
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		List<IndividualWrapper> resultOfComputing = new ArrayList<>();
		for (AID computingAgentI : aidOfComputingAgents) {
						
			IndividualWrapper resultOfComputingI =
					ComputingAgentService.sendAccessesResult(centralManager, computingAgentI, logger);
			resultOfComputing.add(resultOfComputingI);
		}
		
		return resultOfComputing;
	}

	public static ResultsOfComputing sendAccessesResult_(Agent_DistributedEA centralManager,
			IAgentLogger logger) {

		List<IndividualWrapper> results = sendAccessesResult(centralManager, logger);
		
		ResultsOfComputing resultOfComputingAgents = new ResultsOfComputing();
		resultOfComputingAgents.setResultOfComputing(results);
		
		return resultOfComputingAgents;
	}
	/**
	 * Obtains the current results from computing Agent
	 * @param agent
	 * @param computingAgent
	 * @param logger
	 * @return
	 */
	public static IndividualWrapper sendAccessesResult(Agent_DistributedEA agent,
			AID computingAgent, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agentKiller can't be null");
		}

		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgAccessesResult = new ACLMessage(ACLMessage.REQUEST);
		msgAccessesResult.addReceiver(computingAgent);
		msgAccessesResult.setSender(agent.getAID());
		msgAccessesResult.setLanguage(agent.getCodec().getName());
		msgAccessesResult.setOntology(ontology.getName());

		AccessesResult accessesResult = new AccessesResult();
		
		Action action = new Action(agent.getAID(), accessesResult);
		
		try {
			agent.getContentManager().fillContent(msgAccessesResult, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending AccessesResult", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending AccessesResult", e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgAccessesResult);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving ResultOfComputing", e);
			return null;
		}
		
		IndividualWrapper resultOfComputing = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgRetursName);

			resultOfComputing = (IndividualWrapper) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving ResultOfComputing", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving ResultOfComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving ResultOfComputing", e);
		}
		
		return resultOfComputing;
	}
	
	/**
	 * Obtains List of computation-helpmate of one Computing Agent
	 * @param agent
	 * @param computingAgent
	 * @param newStatisticsForEachQuery
	 * @param logger
	 * @return
	 */
	public static HelpmateList sendReportHelpmate(Agent_DistributedEA agent,
			AID computingAgent, boolean newStatisticsForEachQuery, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agentKiller can't be null");
		}

		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgAccessesResult = new ACLMessage(ACLMessage.REQUEST);
		msgAccessesResult.addReceiver(computingAgent);
		msgAccessesResult.setSender(agent.getAID());
		msgAccessesResult.setLanguage(agent.getCodec().getName());
		msgAccessesResult.setOntology(ontology.getName());

		ReportHelpmate reportHelpmate = new ReportHelpmate();
		reportHelpmate.setNewStatisticsForEachQuery(newStatisticsForEachQuery);
		
		Action action = new Action(agent.getAID(), reportHelpmate);
		
		try {
			agent.getContentManager().fillContent(msgAccessesResult, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending AccessesResult", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending AccessesResult", e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgAccessesResult);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving ResultOfComputing", e);
			return null;
		}
		
		HelpmateList helpmateList = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgRetursName);

			helpmateList = (HelpmateList) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving ResultOfComputing", e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving ResultOfComputing", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving ResultOfComputing", e);
		}
		
		return helpmateList;
	}
	
	/**
	 * Sends Computed Solution to neighbors (all another Computing Agents)
	 * @param agent
	 * @param individual
	 * @param logger
	 */
	public static void sendIndividualToNeighbours(Agent_DistributedEA agent,
			IndividualWrapper individual, AgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent sender can't be null");
		}
		
		AID [] aidOfComputingAgents = agent.searchDF(
				Agent_ComputingAgent.class.getName());
		if (aidOfComputingAgents == null) {
			logger.log(Level.INFO, "Computing agent can't find any neighbour " + agent.getAID().getLocalName());
			return;
		}
		List<AID> aidOfComputingAgentsWithoutSender = new ArrayList<AID>(Arrays.asList(aidOfComputingAgents));
		aidOfComputingAgentsWithoutSender.remove(agent.getAID());
		
		AID monitorAID = new AID(AgentNames.MONITOR.getName(), false);
		aidOfComputingAgentsWithoutSender.add(monitorAID);
		
		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgIndividual = new ACLMessage(ACLMessage.INFORM);
		for (AID computingAgentI : aidOfComputingAgentsWithoutSender) {
			msgIndividual.addReceiver(computingAgentI);	
		}
		msgIndividual.setSender(agent.getAID());
		msgIndividual.setLanguage(agent.getCodec().getName());
		msgIndividual.setOntology(ontology.getName());

		
		Action action = new Action(agent.getAID(), individual);
		
		try {
			agent.getContentManager().fillContent(msgIndividual, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending Individual", e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending Individual", e);
		}
		
		agent.send(msgIndividual);
	}
	
	
}
