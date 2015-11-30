package org.distributedea.agents.systemagents.centralmanager.scheduler;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.problem.Problem;

public class SchedulerSimple implements Scheduler {

	int index = 2;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, AgentConfiguration [] configurations,
			Class<?> [] availableProblemTools, AgentLogger logger) {
		
		if (centralManager == null) {
			throw new IllegalArgumentException("centralManager is null");
		}

		if (configurations == null) {
			throw new IllegalArgumentException("configurations is null");
		}
		if (configurations.length == 0) {
			throw new IllegalArgumentException("configurations is empty");
		}
		
		if (problem == null) {
			throw new IllegalStateException("Problem wasn't loaded");
		}
		
		if (availableProblemTools == null) {
			throw new IllegalArgumentException("availableProblemTools is null");
		}
		if (availableProblemTools.length == 0) {
			throw new IllegalArgumentException("availableProblemTools is empty");
		}
		
		if (logger == null) {
			throw new IllegalArgumentException("logger is null");
		}
		
		List<NodeInfo> availableNodes = getAvailableNodes(centralManager, logger);
		
		initializeComputingAgents(centralManager, availableNodes, configurations, logger);
			
		runComputation(centralManager, availableNodes, problem, availableProblemTools, logger);
	}
	
	/**
	 * Make initialization of Computing Agents on nodes
	 * @param centralManager
	 * @param configurations
	 * @param logger
	 */
	private void initializeComputingAgents(Agent_CentralManager centralManager,
			List<NodeInfo> availableNodes, AgentConfiguration [] configurations, AgentLogger logger) {
		
		for (NodeInfo nodeInfoI : availableNodes) {	
			
			AID managerAidI = nodeInfoI.getManagerAgentAID();
			int numberOfCPUI = nodeInfoI.getFreeCPUnumber();
			
			for (int cpuI = 0; cpuI < numberOfCPUI; cpuI++) {
				
				AgentConfiguration agentConfiguration = configurations[index];
				String agentType = agentConfiguration.getAgentType();
				String agentName = agentConfiguration.getAgentName();
				List<Argument> arguments = agentConfiguration.getArguments();
				
				ManagerAgentService.sendCreateAgent(centralManager,
						managerAidI, agentType, agentName, arguments, logger);
			}
		}
		
	}

	private List<NodeInfo> getAvailableNodes(Agent_CentralManager centralManager, AgentLogger logger) {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.requestForNodeInfo(
					centralManager, managerAidI, logger);
			nodeInfos.add(nodeInfoI);
		}
		
		return nodeInfos;
	}
	
	/**
	 * Sends Problem to all computing agents to start computing
	 * @param centralManager
	 * @param problemFileName
	 * @param availableProblemTools
	 * @param logger
	 */
	private void runComputation(Agent_CentralManager centralManager,
			List<NodeInfo> availableNodes, Problem problem,
			Class<?> [] availableProblemTools, AgentLogger logger) {
		
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		int problemToolIndex = 0;
		for (AID computingAgentI : aidComputingAgents) {
			
			Class<?> problemToolClass = availableProblemTools[problemToolIndex];
			problem.setProblemToolClass(problemToolClass.getName());
			
			problemToolIndex = (problemToolIndex +1) % availableProblemTools.length;
			
			ComputingAgentService.sendStartComputing(
					centralManager, computingAgentI, problem, logger);
		}

	}

	/**
	 * Nothing is changing
	 * @param centralManager
	 * @param problemTool
	 * @param logger
	 */
	@Override
	public void replan(Agent_CentralManager centralManager,
			Problem problem, AgentConfiguration [] configurations,
			Class<?> []  availableProblemTools, AgentLogger logger) {

		List<NodeInfo> availableNodes = getAvailableNodes(centralManager, logger);
		initializeComputingAgents(centralManager, availableNodes, configurations, logger);
		
		runComputation(centralManager, availableNodes, problem, availableProblemTools, logger);
		
		
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		List<ResultOfComputing> resultOfComputingAgents = new ArrayList<ResultOfComputing>();
		for (AID computingAgentI : aidOfComputingAgents) {
						
			ResultOfComputing resultOfComputingI =
					ComputingAgentService.sendAccessesResult(centralManager, computingAgentI, logger);
			resultOfComputingAgents.add(resultOfComputingI);
		}
		
		if (resultOfComputingAgents.isEmpty()) {
			logger.logThrowable("No results available", new IllegalStateException());
			return;
		}
		
		ResultOfComputing bestResult = resultOfComputingAgents.get(0);
		ResultOfComputing worstResult = resultOfComputingAgents.get(0);
		
		for (ResultOfComputing resultI : resultOfComputingAgents) {
			
			boolean isMaximalization = problem.isMaximizationProblem();
			if (isMaximalization) {

				if (bestResult.getFitnessValue() < resultI.getFitnessValue()) {
					bestResult = resultI;
				}
				if (worstResult.getFitnessValue() > resultI.getFitnessValue()) {
					worstResult = resultI;
				}
			} else {
				if (bestResult.getFitnessValue() > resultI.getFitnessValue()) {
					bestResult = resultI;
				}
				if (worstResult.getFitnessValue() < resultI.getFitnessValue()) {
					worstResult = resultI;
				}
			}
			
		}
		
		int bestIndex = resultOfComputingAgents.indexOf(bestResult);
		int worstIndex = resultOfComputingAgents.indexOf(worstResult);
		
		AID bestAID = aidOfComputingAgents[bestIndex];
		AID worstAID = aidOfComputingAgents[worstIndex];
		
		// kill worst agent
		ManagerAgentService.sendKillAgent(centralManager, worstAID, logger);
	
		// wait for kill and unregister agent
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.logThrowable("Error by waiting for killing agent " + worstAID.getLocalName(), e);
			throw new IllegalStateException("Error by waiting for killing agent " + worstAID.getLocalName());
		}

		AID manager = ManagerAgentService.getManagerAgentOfAID(centralManager, worstAID);
		
		AgentConfiguration agentConfiguration = configurations[index];
		String agentType = agentConfiguration.getAgentType();
		String agentName = agentConfiguration.getAgentName();
		List<Argument> arguments = agentConfiguration.getArguments();
	
		// create new agent
		AID newAgent = ManagerAgentService.sendCreateAgent(centralManager,
				manager, agentType, agentName, arguments, logger);
		
		// wait for initialization agent
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.logThrowable("Error by waiting for new agent initialization " + newAgent.getLocalName(), e);
			throw new IllegalStateException("Error by waiting for new agent initialization " + newAgent.getLocalName());
		}
		
		// start computing
		ComputingAgentService.sendStartComputing(
				centralManager, newAgent, problem, logger);
//*/
	}
	

}
