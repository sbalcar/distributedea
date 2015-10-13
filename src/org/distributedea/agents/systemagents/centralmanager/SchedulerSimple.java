package org.distributedea.agents.systemagents.centralmanager;

import jade.core.AID;

import java.util.List;

import org.distributedea.agents.computingagents.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;

public class SchedulerSimple implements Scheduler {

	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			AgentConfiguration [] configurations, String problemFileName,
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
		
		if (availableProblemTools == null) {
			throw new IllegalArgumentException("availableProblemTools is null");
		}
		if (availableProblemTools.length == 0) {
			throw new IllegalArgumentException("availableProblemTools is empty");
		}
		
		if (logger == null) {
			throw new IllegalArgumentException("logger is null");
		}
		
		initializeComputingAgents(centralManager, configurations, logger);
			
		runComputation(centralManager, problemFileName, availableProblemTools, logger);
	}
	
	/**
	 * Make initialization of Computing Agents on nodes
	 * @param centralManager
	 * @param configurations
	 * @param logger
	 */
	private void initializeComputingAgents(Agent_CentralManager centralManager,
			AgentConfiguration [] configurations, AgentLogger logger) {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.sendNodeInfo(
					centralManager, managerAidI, logger);
			
			int numberOfCPUI = nodeInfoI.getNumberCPU();
			
			for (int cpuI = 0; cpuI < numberOfCPUI; cpuI++) {
				
				AgentConfiguration agentConfiguration = configurations[2];
				String agentType = agentConfiguration.getAgentType();
				String agentName = agentConfiguration.getAgentName();
				List<Argument> arguments = agentConfiguration.getArguments();
				
				ManagerAgentService.sendCreateAgent(centralManager,
						managerAidI, agentType, agentName, arguments, logger);
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
	private void runComputation(Agent_CentralManager centralManager, String problemFileName,
			Class<?> [] availableProblemTools, AgentLogger logger) {
		
		Class<?> problemToolClass1 = availableProblemTools[0];
		ProblemTool problemTool = null;
		try {
			problemTool = (ProblemTool) problemToolClass1.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.logThrowable("ProblemTool wasn't initialized", e);
			throw new IllegalStateException("ProblemTool wasn't initialized");
		}
		
		Problem problem = problemTool.readProblem(problemFileName, logger);
		
		if (problem == null) {
			throw new IllegalStateException("Problem wasn't loaded");
		}

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
	public void replan(Agent_CentralManager centralManager, AgentLogger logger) {

		//ResultOfComputing resultOfComputing =
		//		ComputingAgentService.sendAccessesResult(centralManager, computingAgent, logger);

	}

	public int numberOfAvailableCPU(Agent_CentralManager centramManager,
			AgentLogger logger) {
		
		AID [] aidManagerAgents = centramManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		int numberOfCPU = 0;
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.sendNodeInfo(
					centramManager, managerAidI, logger);
			
			numberOfCPU += nodeInfoI.getNumberCPU();
		}
		
		return numberOfCPU;
	}

}
