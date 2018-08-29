package org.distributedea.services;

import java.io.IOException;
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
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;

import org.distributedea.AgentNames;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_Monitor;
import org.distributedea.agents.systemagents.centralmanager.structures.helpers.ModelOfHelpmates;
import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.agentinfo.AgentInfoWrapper;
import org.distributedea.ontology.agentinfo.AgentInfosWrapper;
import org.distributedea.ontology.agentinfo.GetAgentInfo;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.helpmate.StatisticOfHelpmates;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;
import org.distributedea.ontology.islandmodel.AIDs;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.management.ReadyToBeKilled;
import org.distributedea.ontology.management.PrepareYourselfToKill;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;


/**
 * Structure of services offering all descendants
 * of {@link Agent_ComputingAgent} 
 * @author stepan
 *
 */
public class ComputingAgentService {

	/**
	 * Returns information about best computing helpers at all nodes
	 * @param centralManager
	 * @param newStatisticForEachQuery
	 * @param logger
	 * @return
	 */
	public static ModelOfHelpmates getHelpmates(Agent_CentralManager centralManager,
			boolean newStatisticForEachQuery, IAgentLogger logger) {
		
		if (centralManager == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		// search all Computing Agents
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		

		List<StatisticOfHelpmates> statisticOfHelpmates = new ArrayList<>();
		
		// going through all computing agents
		for (AID aidComputingAgentI : aidComputingAgents) {
			
			StatisticOfHelpmates helpmateListI = 
					ComputingAgentService.sendReportHelpmate(
							centralManager, aidComputingAgentI,
							newStatisticForEachQuery, logger);
			statisticOfHelpmates.add(helpmateListI);
		}
		
		return new ModelOfHelpmates(statisticOfHelpmates);
	}
	
	/**
	 * Obtains {@link AgentInfosWrapper} from all running {@link Agent_ComputingAgent}
	 * @param agentSender
	 * @param logger
	 * @return
	 */
	public static AgentInfosWrapper getAgentInfos(Agent_CentralManager agentSender,
			IAgentLogger logger) {
	
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		// Get Result of Computing
		AID [] aidOfComputingAgents = agentSender.searchDF(
				Agent_ComputingAgent.class.getName());

		List<AgentInfoWrapper> agentInfoWrappers = new ArrayList<>();
		for (AID computingAgentI : aidOfComputingAgents) {
			
			AgentInfoWrapper agentInfoI =
					sendGetAgentInfo(agentSender, computingAgentI, logger);
			
			agentInfoWrappers.add(agentInfoI);
		}

		return new AgentInfosWrapper(agentInfoWrappers);
	}
	
	/**
	 * Obtains {@link AgentInfoWrapper} from the specified {@link Agent_ComputingAgent}
	 * @param agentSender
	 * @param computingAgent
	 * @param problemStruct
	 * @param logger
	 * @return
	 */
	public static AgentInfoWrapper sendGetAgentInfo(Agent_CentralManager agentSender,
			AID computingAgent, IAgentLogger logger) {
		
		if (agentSender == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (computingAgent == null) {
			throw new IllegalArgumentException("Argument " + 
					AID.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		Ontology ontology = ManagementOntology.getInstance();

		ACLMessage msgGetAgentInfo = new ACLMessage(ACLMessage.REQUEST);
		msgGetAgentInfo.addReceiver(computingAgent);
		msgGetAgentInfo.setSender(agentSender.getAID());
		msgGetAgentInfo.setLanguage(agentSender.getCodec().getName());
		msgGetAgentInfo.setOntology(ontology.getName());

		
		GetAgentInfo getAgentInfo = new GetAgentInfo();
		
		Action action = new Action(agentSender.getAID(), getAgentInfo);
		
		try {
			agentSender.getContentManager().fillContent(msgGetAgentInfo, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					GetAgentInfo.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					GetAgentInfo.class.getSimpleName(), e);
		}
		
		
		ACLMessage msgAgentInfo = null;
		try {
			msgAgentInfo = FIPAService
					.doFipaRequestClient(agentSender, msgGetAgentInfo);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving " +
					AgentInfoWrapper.class.getSimpleName(), e);
			return null;
		}
		
		AgentInfoWrapper agentInfoWrp = null;
		try {
			Result result = (Result)
					agentSender.getContentManager().extractContent(msgAgentInfo);

			agentInfoWrp = (AgentInfoWrapper) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving " +
					AgentInfoWrapper.class.getSimpleName(), e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving " +
					AgentInfoWrapper.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving " +
					AgentInfoWrapper.class.getSimpleName(), e);
		}
		
		return agentInfoWrp;

	}
	
	/**
	 * Sends message {@link StartComputing} contains Problem definition
	 * to {@link Agent_ComputingAgent}
	 * @param agent
	 * @param computingAgentAID
	 * @param problemStruct
	 * @param islandModelConf
	 * @param logger
	 * @return
	 */
	public static boolean sendStartComputing(Agent_DistributedEA agent,
			AID computingAgentAID, ProblemWrapper problemWrp,
			IslandModelConfiguration islandModelConf, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent.class.getName() + "can't be null");
		}
		if (computingAgentAID == null) {
			throw new IllegalArgumentException("Argument " +
					AID.class.getName() + "can't be null");
		}
		if (problemWrp == null || ! problemWrp.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					ProblemWrapper.class.getSimpleName() + " is not valid");
		}
		if (islandModelConf == null || ! islandModelConf.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		Ontology ontology = ComputingOntology.getInstance();

		ACLMessage msgStartComputing = new ACLMessage(ACLMessage.REQUEST);
		msgStartComputing.addReceiver(computingAgentAID);
		msgStartComputing.setSender(agent.getAID());
		msgStartComputing.setLanguage(agent.getCodec().getName());
		msgStartComputing.setOntology(ontology.getName());
		
		
		StartComputing startComputing = new StartComputing(
				problemWrp, islandModelConf);
		
		Action action = new Action(agent.getAID(), startComputing);
		
		try {
			agent.getContentManager().fillContent(msgStartComputing, action);
		
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					StartComputing.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					StartComputing.class.getSimpleName(), e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgStartComputing);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving " +
					StartComputing.class.getSimpleName() + " answer", e);
			return false;
		}
		
		if (msgRetursName.getPerformative() == ACLMessage.INFORM) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sends message {@link PrepareYourselfToKill} to prepare
	 * {@link Agent_ComputingAgent} to be killed.
	 * @param agent
	 * @param computingAgent
	 * @param logger
	 * @return
	 */
	public static ReadyToBeKilled sendPrepareYourselfToKill(
			Agent_DistributedEA agent, AID computingAgent, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
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
			logger.logThrowable("CodecException by sending " +
					PrepareYourselfToKill.class.getSimpleName(), e);
			return null;
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					PrepareYourselfToKill.class.getSimpleName(), e);
			return null;
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgPrepareYourselfToKill);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving " +
					ReadyToBeKilled.class.getSimpleName(), e);
			return null;
		}
		
		ReadyToBeKilled everythingPreparedToBeKilled = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgRetursName);

			everythingPreparedToBeKilled = (ReadyToBeKilled) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving " +
					ReadyToBeKilled.class.getSimpleName(), e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving " +
					ReadyToBeKilled.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving " +
					ReadyToBeKilled.class.getSimpleName(), e);
		}
		
		return everythingPreparedToBeKilled;
	}
	
	/**
	 * Obtains the current results from all computing Agents
	 * @param centralManager
	 * @param logger
	 * @return
	 */
	public static List<IndividualWrapper> sendAccessesResult(
			Agent_DistributedEA centralManager, IAgentLogger logger) {
		
		if (centralManager == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		// Get Result of Computing
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		List<IndividualWrapper> resultOfComputing = new ArrayList<>();
		for (AID computingAgentI : aidOfComputingAgents) {
						
			IndividualWrapper resultOfComputingI =
					ComputingAgentService.sendAccessesResult(centralManager, computingAgentI, logger);
			if (resultOfComputingI != null) {
				resultOfComputing.add(resultOfComputingI);
			}
		}
		
		return resultOfComputing;
	}

	/**
	 * Returns information about the current computing result at all nodes
	 * @param centralManager
	 * @param logger
	 * @return
	 */
	public static IndividualsWrappers getResultOfComputings(
			Agent_CentralManager centralManager, AgentLogger logger) {
		
		if (centralManager == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
				
		List<IndividualWrapper> individuals = new ArrayList<>();
		
		for (AID computingAgentI : aidOfComputingAgents) {
						
			IndividualWrapper resultOfComputingI = null;
			try {
				resultOfComputingI = ComputingAgentService.sendAccessesResult(
						centralManager, computingAgentI, logger);
			} catch (Exception e) {}
			
			if (resultOfComputingI != null) {
				individuals.add(resultOfComputingI);
			}
		}
		
		return new IndividualsWrappers(individuals);
	}

	public static IndividualsWrappers sendAccessesResult_(
			Agent_DistributedEA centralManager, IAgentLogger logger) {

		List<IndividualWrapper> results = sendAccessesResult(centralManager, logger);
		
		return new IndividualsWrappers(results);
	}
	
	/**
	 * Obtains the current results from {@link Agent_ComputingAgent}
	 * @param agent
	 * @param computingAgent
	 * @param logger
	 * @return
	 */
	public static IndividualWrapper sendAccessesResult(Agent_DistributedEA agent,
			AID computingAgent, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
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
			logger.logThrowable("CodecException by sending " +
					AccessesResult.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					AccessesResult.class.getSimpleName(), e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgAccessesResult);
		} catch (RefuseException e) {
			return null;
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving ResultOfComputing", e);
			return null;
		}
		
		if (msgRetursName.getPerformative() != ACLMessage.INFORM) {
			return null;
		}
		
		IndividualWrapper individualWrp = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgRetursName);

			individualWrp = (IndividualWrapper) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving " +
					IndividualWrapper.class.getSimpleName(), e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving " +
					IndividualWrapper.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving " +
					IndividualWrapper.class.getSimpleName(), e);
		}
		
		return individualWrp;
	}
	
	/**
	 * Obtains List of computation-helpmate of one Computing Agent
	 * @param agent
	 * @param computingAgent
	 * @param newStatisticsForEachQuery
	 * @param logger
	 * @return
	 */
	public static StatisticOfHelpmates sendReportHelpmate(Agent_DistributedEA agent,
			AID computingAgent, boolean newStatisticsForEachQuery, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (computingAgent == null) {
			throw new IllegalArgumentException("Argument " +
					AID.class.getSimpleName() + " can't be null");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}

		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgAccessesResult = new ACLMessage(ACLMessage.REQUEST);
		msgAccessesResult.addReceiver(computingAgent);
		msgAccessesResult.setSender(agent.getAID());
		msgAccessesResult.setLanguage(agent.getCodec().getName());
		msgAccessesResult.setOntology(ontology.getName());

		ReportHelpmate reportHelpmate =
				new ReportHelpmate(newStatisticsForEachQuery);
		
		Action action = new Action(agent.getAID(), reportHelpmate);
		
		try {
			agent.getContentManager().fillContent(msgAccessesResult, action);
			
		} catch (Codec.CodecException e) {
			logger.logThrowable("CodecException by sending " +
					AccessesResult.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by sending " +
					AccessesResult.class.getSimpleName(), e);
		}
		
		ACLMessage msgRetursName = null;
		try {
			msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgAccessesResult);
		} catch (FIPAException e) {
			logger.logThrowable("FIPAException by receiving " +
					StatisticOfHelpmates.class.getSimpleName(), e);
			return null;
		}
		
		StatisticOfHelpmates helpmateList = null;
		try {
			Result result = (Result)
					agent.getContentManager().extractContent(msgRetursName);

			helpmateList = (StatisticOfHelpmates) result.getValue();

		} catch (UngroundedException e) {
			logger.logThrowable("UngroundedException by receiving " +
					StatisticOfHelpmates.class.getSimpleName(), e);
		} catch (CodecException e) {
			logger.logThrowable("CodecException by receiving " +
					StatisticOfHelpmates.class.getSimpleName(), e);
		} catch (OntologyException e) {
			logger.logThrowable("OntologyException by receiving " +
					StatisticOfHelpmates.class.getSimpleName(), e);
		}
		
		return helpmateList;
	}
	
	
	/**
	 * Sends Computed Solution to neighbors (all another {@link Agent_ComputingAgent})
	 * @param agent
	 * @param individual
	 * @param logger
	 */
	public static void sendIndividualToNeighboursAndToMonitor(Agent_DistributedEA agent,
			IndividualWrapper individual, IslandModelConfiguration islandModel, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (individual == null || ! individual.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		
		AID [] aidOfComputingAgents = agent.searchDF(
				Agent_ComputingAgent.class.getName());
		if (aidOfComputingAgents == null) {
			logger.log(Level.INFO, "Computing agent can't find any neighbour " + agent.getAID().getLocalName());
			return;
		}
		List<AID> aidOfComputingAgentsWithoutSender =
				new ArrayList<AID>(Arrays.asList(aidOfComputingAgents));
		aidOfComputingAgentsWithoutSender.remove(agent.getAID());
		
		
		AID monitorAID = new AID(AgentNames.MONITOR.getName(), false);
		aidOfComputingAgentsWithoutSender.add(monitorAID);
		
		AIDs aidOfCAsObj = new AIDs(aidOfComputingAgentsWithoutSender);
		aidOfCAsObj = aidOfCAsObj.exportRanomAIDs(3);
		
		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgIndividual = new ACLMessage(ACLMessage.INFORM);
		for (AID computingAgentI : aidOfCAsObj.getAgentIDs()) {
			msgIndividual.addReceiver(computingAgentI);	
		}
		msgIndividual.setSender(agent.getAID());
		msgIndividual.setLanguage(agent.getCodec().getName());
		msgIndividual.setOntology(ontology.getName());

		// sends individual as object
		try {
			msgIndividual.setContentObject(individual);
		} catch (Exception e) {
			logger.logThrowable("IOException by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		}
		
		agent.send(msgIndividual);
	}
	
	/**
	 * Sends Computed Solution to neighbors (all another {@link Agent_ComputingAgent})
	 * @param agent
	 * @param individual
	 * @param logger
	 */
	public static void sendIndividualToNeighbours(Agent_DistributedEA agent,
			IndividualWrapper individual, IslandModelConfiguration islandModel, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (individual == null || ! individual.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
		
		
		AID [] aidOfComputingAgents = agent.searchDF(
				Agent_ComputingAgent.class.getName());
		if (aidOfComputingAgents == null) {
			logger.log(Level.INFO, "Computing agent can't find any neighbour " + agent.getAID().getLocalName());
			return;
		}
		List<AID> aidOfComputingAgentsWithoutSender =
				new ArrayList<AID>(Arrays.asList(aidOfComputingAgents));
		aidOfComputingAgentsWithoutSender.remove(agent.getAID());
		
		AIDs aidOfCAsObj = new AIDs(aidOfComputingAgentsWithoutSender);
		aidOfCAsObj = aidOfCAsObj.exportRanomAIDs(
				islandModel.getNeighbourCount());
		
		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgIndividual = new ACLMessage(ACLMessage.INFORM);
		for (AID computingAgentI : aidOfCAsObj.getAgentIDs()) {
			msgIndividual.addReceiver(computingAgentI);	
		}
		msgIndividual.setSender(agent.getAID());
		msgIndividual.setLanguage(agent.getCodec().getName());
		msgIndividual.setOntology(ontology.getName());

		// sends individual as object
		try {
			msgIndividual.setContentObject(individual);
		} catch (Exception e) {
			logger.logThrowable("IOException by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		}
		
		agent.send(msgIndividual);
	}
	
	/**
	 * Sends {@link IndividualWrapper} as the solution to {@link Agent_Monitor}
	 * @param agent
	 * @param individual
	 * @param logger
	 */
	public static void sendIndividualToMonitor(Agent_DistributedEA agent,
			IndividualWrapper individual, IAgentLogger logger) {
		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can't be null");
		}
		if (individual == null || ! individual.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can't be null");
		}
				
		AID monitorAID = new AID(AgentNames.MONITOR.getName(), false);
		Ontology ontology = ResultOntology.getInstance();

		ACLMessage msgIndividual = new ACLMessage(ACLMessage.INFORM);
		msgIndividual.addReceiver(monitorAID);	
		msgIndividual.setSender(agent.getAID());
		msgIndividual.setLanguage(agent.getCodec().getName());
		msgIndividual.setOntology(ontology.getName());

		// sends individual as object
		try {
			msgIndividual.setContentObject(individual);
		} catch (IOException e) {
			logger.logThrowable("Exception by sending " +
					IndividualWrapper.class.getSimpleName(), e);
		}
		
		agent.send(msgIndividual);
	}
	
}
