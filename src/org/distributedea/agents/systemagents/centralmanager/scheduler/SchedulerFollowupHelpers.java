package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.logging.Level;

import jade.core.AID;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitialization;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.Iteration;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.HelpmatesWrapper;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class SchedulerFollowupHelpers implements Scheduler {

	private SchedulerInitialization schedullerInit = null;
	
	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	
	public SchedulerFollowupHelpers() {} // for serialization
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws SchedulerException {
		
		logger.log(Level.INFO, "Scheduler " + getClass().getSimpleName() + " initialization");
		
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
		
		HelpmatesWrapper helpmates = SchedulerTool.getHelpmates(
				centralManager, NEW_STATISTICS_FOR_EACH_QUERY, logger);
		
		Pair<AgentDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		AgentDescription minPriorityDescription = minPriorityPair.first;
		int minPriority = minPriorityPair.second;
		
		Pair<AgentDescription, Integer> maxPriorityPair =
				helpmates.exportMaxPrioritizedDescription();
		AgentDescription maxPriorityDescription = maxPriorityPair.first;
		int maxPriority = maxPriorityPair.second;
		
		String minPriorityAgentName = minPriorityDescription.getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The worst: " + minPriorityAgentName + " priority: " + minPriority);

		String maxPriorityAgentName = maxPriorityDescription.getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The best : " + maxPriorityAgentName + " priority: " + maxPriority);
		
		// agent configurations
		AgentConfiguration bestConfiguration = maxPriorityDescription.getAgentConfiguration();
		AgentConfiguration worstConfiguration = minPriorityDescription.getAgentConfiguration();

		// aid of the worst agent (agent to kill)
		AID worstAID = worstConfiguration.exportAgentAID();
		
		Class<?> bestProblemToolClass = maxPriorityDescription.exportProblemToolClass();
		
		
		Pair<AgentConfiguration, Class<?>> newMethod =
				cooseNewMethod(bestConfiguration, bestProblemToolClass);
		AgentConfiguration newAgentConfiguration = newMethod.first;
		Class<?> newProblemToolClass = newMethod.second;
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
		
		// kill agent with the smallest priority and run the agent with the highest priority
		SchedulerTool.killAndCreateAgent(centralManager, worstAID,
				newAgentConfiguration, problemStruct, logger);

	}
	
	private Pair<AgentConfiguration, Class<?>> cooseNewMethod(
			AgentConfiguration bestConfiguration, Class<?> bestProblemToolClass) {

		AgentDescription candidateDescription = schedullerInit.removeNextCandidate();
		
		if (candidateDescription != null) {
			
			return new Pair<AgentConfiguration, Class<?>>(
					candidateDescription.getAgentConfiguration(),
					candidateDescription.exportProblemToolClass());
		}
		
		return new Pair<AgentConfiguration, Class<?>>(
				bestConfiguration, bestProblemToolClass);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
	}
	
}
