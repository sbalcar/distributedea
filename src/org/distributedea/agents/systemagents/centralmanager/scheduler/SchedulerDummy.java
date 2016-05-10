package org.distributedea.agents.systemagents.centralmanager.scheduler;


import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.noontology.Job;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

/**
 * Dummy Scheduler which runs on one node
 * one Computing Agent with one ProblemTool
 *
 */
public class SchedulerDummy implements Scheduler {
	
	int NODE_INDEX = 0;
	
	/** index on the method in the list of methods (configuration/methods.xml) */
	int COMPUTING_AGENT_INDEX = 1;
	
	/** index on the Problem Tool in the list of tolls */
	int PROBLEM_TOOL_INDEX = 0;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Job job, AgentConfigurations configurations,
			AgentLogger logger) throws SchedulerException {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		// chooses node
		AID managerAidI;
		try {
			managerAidI = aidManagerAgents[NODE_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SchedulerException("Manager agent to create Computing Agent not available");
		}
		

		// chooses agent configuration
		AgentConfiguration agentConfiguration;
		try {
			agentConfiguration = configurations.getAgentConfigurations().get(COMPUTING_AGENT_INDEX);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SchedulerException("Computing agent Configuration not available");
		}
		
		ManagerAgentService.sendCreateAgent(centralManager,
				managerAidI, agentConfiguration, logger);
		
		
		
		// chooses ProblemTool
		Class<?> problemToolI = job.getProblemTools().getProblemTools().get(PROBLEM_TOOL_INDEX);
		
		// assumes the existence of only one Computing Agent
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		AID computingAgent = aidComputingAgents[0];
		
		ProblemStruct problemStruct = new ProblemStruct();
		problemStruct.setJobID(job.getJobID());
		problemStruct.setIndividualDistribution(job.getIndividualDistribution());
		problemStruct.setProblem(job.getProblem());
		problemStruct.setProblemToolClass(problemToolI.getName());
		
		ComputingAgentService.sendStartComputing(
				centralManager, computingAgent, problemStruct, logger);
		

	}

	
	@Override
	public void replan(Agent_CentralManager centralManager, Job job,
			AgentConfigurations configurations,
			AgentLogger logger) throws SchedulerException {
	}

	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}


}
