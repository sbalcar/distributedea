package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problem.Problem;

public class SchedulerRunEachMethodOnce implements Scheduler {

	int NODE_INDEX = 0;
	
	/** index on the Problem Tool in the list of tolls */
	int PROBLEM_TOOL_INDEX = 0;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, List<AgentConfiguration> configurations,
			List<Class<?>> availablProblemTools, AgentLogger logger) throws SchedulerException {

		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		// chooses node
		AID managerAidI;
		try {
			managerAidI = aidManagerAgents[NODE_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SchedulerException("Manager agent to create Computing Agent not available");
		}
		

		// create one agent for each configuration
		for (AgentConfiguration agentConfigurationI : configurations) {
						
			ManagerAgentService.sendCreateAgent(centralManager,
					managerAidI, agentConfigurationI, logger);

		}
		

		// chooses ProblemTool by index
		Class<?> problemToolI;
		try {
			problemToolI = availablProblemTools.get(PROBLEM_TOOL_INDEX);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SchedulerException("ProblemTool not available");
		}
		
		// search all Computing Agents
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
			List<AgentConfiguration> configurations,
			List<Class<?>> availableProblemTools, AgentLogger logger) {
	}

	@Override
	public boolean continueWithComputingInTheNextGeneration() {
		return true;
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
	
}
