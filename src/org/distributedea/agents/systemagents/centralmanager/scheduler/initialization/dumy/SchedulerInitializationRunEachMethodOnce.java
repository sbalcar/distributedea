package org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.dumy;


import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class SchedulerInitializationRunEachMethodOnce implements Scheduler {

	int NODE_INDEX = 0;
	
	/** index on the Problem Tool in the list of tolls */
	int PROBLEM_TOOL_INDEX = 0;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws SchedulerException {

		
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
		AgentConfigurations configurations = job.getAgentConfigurations();
		for (AgentConfiguration agentConfigurationI : configurations.getAgentConfigurations()) {
						
			ManagerAgentService.sendCreateAgent(centralManager,
					managerAidI, agentConfigurationI, logger);

		}
		
		// chooses ProblemTool by index
		Class<?> problemToolI = job.getProblemTools().getProblemTools().get(PROBLEM_TOOL_INDEX);
		
		// search all Computing Agents
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		// start computing in all created computing agents
		for (AID aidComputingAgentI : aidComputingAgents) {
			
			ProblemStruct problemStruct = new ProblemStruct();
			problemStruct.setJobID(job.getJobID());
			problemStruct.setIndividualDistribution(job.getIndividualDistribution());
			problemStruct.setProblem(job.getProblem());
			problemStruct.setProblemToolClass(problemToolI.getName());
			
			ComputingAgentService.sendStartComputing(
					centralManager, aidComputingAgentI, problemStruct, logger);

		}
		
	}

	
	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			AgentLogger logger) {
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}
	
}
