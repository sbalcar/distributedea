package org.distributedea.agents.systemagents.centralmanager.scheduler;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemToolEvaluation;

public class SchedulerSimple implements Scheduler {

	int index = 2;
	
	private String jobID;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, String jobID, List<AgentConfiguration> configurations,
			List<Class<?>> availableProblemTools, AgentLogger logger) throws SchedulerException {
		
		this.jobID = jobID;
		
		List<NodeInfo> availableNodes = SchedulerTool.getAvailableNodes(centralManager, logger);
		
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
			List<NodeInfo> availableNodes, List<AgentConfiguration> configurations, AgentLogger logger) {
		
		for (NodeInfo nodeInfoI : availableNodes) {	
			
			AID managerAidI = nodeInfoI.getManagerAgentAID();
			int numberOfCPUI = nodeInfoI.getFreeCPUnumber();
			
			for (int cpuI = 0; cpuI < numberOfCPUI; cpuI++) {
				
				AgentConfiguration agentConfiguration = configurations.get(index);
				
				ManagerAgentService.sendCreateAgent(centralManager,
						managerAidI, agentConfiguration, logger);
			}
		}
		
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
			List<Class<?>> availableProblemTools, AgentLogger logger) {
		
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		int problemToolIndex = 0;
		for (AID computingAgentI : aidComputingAgents) {
			
			Class<?> problemToolClass = availableProblemTools.get(problemToolIndex);
			
			problemToolIndex = (problemToolIndex +1) % availableProblemTools.size();
			
			ComputingAgentService.sendStartComputing(
					centralManager, computingAgentI, problem, problemToolClass, jobID, logger);
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
			Problem problem, List<AgentConfiguration> configurations,
			List<Class<?>> availableProblemTools, AgentLogger logger)  throws SchedulerException {

		List<NodeInfo> availableNodes = SchedulerTool.getAvailableNodes(centralManager, logger);
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
			logger.logThrowable("No results available", new SchedulerException());
			throw new SchedulerException("No results available");
		}
		
		ResultOfComputing bestResult = resultOfComputingAgents.get(0);
		ResultOfComputing worstResult = resultOfComputingAgents.get(0);
		
		for (ResultOfComputing resultI : resultOfComputingAgents) {
			
			double bestFitnessI = bestResult.getFitnessValue();
			double worstFitnessI = worstResult.getFitnessValue();
			double fitnessNew = resultI.getFitnessValue();
			
			boolean isNewIndividualBetter =
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							fitnessNew, bestFitnessI, problem);
			boolean isNewIndividualWorse =
					ProblemToolEvaluation.isFistFitnessWorseThanSecond(
							fitnessNew, worstFitnessI, problem);
			if (isNewIndividualBetter) {
				bestResult = resultI;
			}
			if (isNewIndividualWorse) {
				worstResult = resultI;
			}
		}
		
		int worstIndex = resultOfComputingAgents.indexOf(worstResult);
		AID worstAID = aidOfComputingAgents[worstIndex];

		AgentConfiguration bestConfiguration = bestResult.exportAgentConfiguration();
		AgentDescription bestDescription = bestResult.getAgentDescription();
		Class<?> bestProblemToolClass = bestDescription.exportProblemToolClass();
		String jobID = bestResult.getJobID();
		
		SchedulerTool.killAndCreateAgent(centralManager, worstAID,
				bestConfiguration, problem, bestProblemToolClass, jobID, logger);
	}
	
	@Override
	public boolean continueWithComputingInTheNextGeneration() {
		return true;
	}

	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
