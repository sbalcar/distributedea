package org.distributedea.agents.systemagents.centralmanager.scheduler;

import jade.core.AID;

import java.util.List;
import java.util.Random;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitialization;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.Iteration;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class SchedulerRandom implements Scheduler {
	
	private SchedulerInitialization schedullerInit = null;
	
	Random ran = new Random();
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager, JobRun job,
			AgentLogger logger) throws SchedulerException {
		
		SchedulerInitializationState state = SchedulerInitializationState.RUN_ONE_AGENT_PER_CORE;
		
		schedullerInit = new SchedulerInitialization(state, true);
		schedullerInit.agentInitialization(centralManager, job, logger);
	}

	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			 Iteration iteration, ReceivedData receivedData, AgentLogger logger
			 ) throws SchedulerException {		
		
		// initialization of Methods on the new containers
		schedullerInit.replan(centralManager, job, iteration, receivedData, logger);
		
		//random select agent to kill
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		int indexAID = ran.nextInt(aidComputingAgents.length);
		AID agentToKillAID = aidComputingAgents[indexAID];
		
		
		//random select configuration for creating the new agent
		AgentConfigurations configurations = job.getAgentConfigurations();
				
		//random select problem tool for the new computation
		List<Class<?>> problemTools = job.getProblemTools().getProblemTools();

		
		Pair<AgentConfiguration, Class<?>> newMethod =
				cooseNewMethod(configurations, problemTools);
		AgentConfiguration agentToCreate = newMethod.first;
		Class<?> problemToolClass = newMethod.second;
				
		ProblemStruct problemStruct = job.exportProblemStruct(problemToolClass);
				
		SchedulerTool.killAndCreateAgent(centralManager, agentToKillAID,
				agentToCreate, problemStruct, logger);
	}
	
	private Pair<AgentConfiguration, Class<?>> cooseNewMethod(AgentConfigurations configurations, List<Class<?>> problemTools) {
		
		AgentDescription candidateDescription = schedullerInit.removeNextCandidate();
		
		if (candidateDescription != null) {
			
			return new Pair<AgentConfiguration, Class<?>>(
					candidateDescription.getAgentConfiguration(),
					candidateDescription.exportProblemToolClass());
		}
		
		int indexAC = ran.nextInt(configurations.getAgentConfigurations().size());
		AgentConfiguration agentToCreate = configurations.exportAgentConfigurations(indexAC);
		
		int indexPT = ran.nextInt(problemTools.size());
		Class<?> problemToolClass = problemTools.get(indexPT);
		
		return new Pair<AgentConfiguration, Class<?>>(
				agentToCreate, problemToolClass);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}
}
