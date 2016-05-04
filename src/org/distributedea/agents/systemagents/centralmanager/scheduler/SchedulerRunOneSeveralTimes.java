package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.List;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problem.Problem;

public class SchedulerRunOneSeveralTimes implements Scheduler {

	int NUBER_OF_AGENT = 5;
	
	/** index on the method(agent)*/
	int METHOD_INDEX = 0;
	
	/** index on the Problem Tool in the list of tolls */
	int PROBLEM_TOOL_INDEX = 0;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, String jobID, List<AgentConfiguration> configurations,
			List<Class<?>> availablProblemTools, AgentLogger logger) throws SchedulerException {

		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		AgentConfiguration agentConfigurationI = configurations.get(METHOD_INDEX);

		// create set of agents for each agent Manager
		for (AID managerAidI : aidManagerAgents) {
			
			for (int agnetNumeberI = 0; agnetNumeberI < NUBER_OF_AGENT; agnetNumeberI++) {
				
				ManagerAgentService.sendCreateAgent(centralManager,
						managerAidI, agentConfigurationI, logger);
			}
		}
		
		
		// chooses ProblemTool by index
		Class<?> problemToolI;
		try {
			problemToolI = availablProblemTools.get(PROBLEM_TOOL_INDEX);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SchedulerException("ProblemTool not available");
		}
		
		// assumes the existence of only one Computing Agent
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		// start computing in all created computing agents
		for (AID aidComputingAgentI : aidComputingAgents) {
			
			ComputingAgentService.sendStartComputing(
					centralManager, aidComputingAgentI, problem, problemToolI, jobID, logger);

		}	
		
	}

	@Override
	public void replan(Agent_CentralManager centralManager, Problem problem,
			List<AgentConfiguration> configurations,
			List<Class<?>> availableProblemTools, AgentLogger logger) throws SchedulerException {		
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
