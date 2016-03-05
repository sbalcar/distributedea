package org.distributedea.agents.systemagents.centralmanager.scheduler;

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
import org.distributedea.ontology.problem.Problem;

public class SchedulerRunEachOnce implements Scheduler {

	int NODE_INDEX = 0;
	
	/** index on the Problem Tool in the list of tolls */
	int PROBLEM_TOOL_INDEX = 0;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, AgentConfiguration[] configurations,
			Class<?>[] availablProblemTools, AgentLogger logger) {

		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		// chooses node
		AID managerAidI;
		try {
			managerAidI = aidManagerAgents[NODE_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException("Manager agent to create Computing Agent not available");
		}
		

		// create one agent for each configuration
		for (AgentConfiguration agentConfigurationI : configurations) {
					
			String agentType = agentConfigurationI.getAgentType();
			String agentName = agentConfigurationI.getAgentName();
			List<Argument> arguments = agentConfigurationI.getArguments();
			
			ManagerAgentService.sendCreateAgent(centralManager,
					managerAidI, agentType, agentName, arguments, logger);

		}
		

		// chooses ProblemTool by index
		Class<?> problemToolI;
		try {
			problemToolI = availablProblemTools[PROBLEM_TOOL_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException("ProblemTool not available");
		}
		
		// assumes the existence of only one Computing Agent
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		// start computing in all created computing agents
		for (AID aidComputingAgentI : aidComputingAgents) {
						
			problem.setProblemToolClass(problemToolI.getName());
			ComputingAgentService.sendStartComputing(
					centralManager, aidComputingAgentI, problem, logger);

		}		
		
	}

	
	
	@Override
	public void replan(Agent_CentralManager centralManager, Problem problem,
			AgentConfiguration[] configurations,
			Class<?>[] availableProblemTools, AgentLogger logger) {
	}

}
