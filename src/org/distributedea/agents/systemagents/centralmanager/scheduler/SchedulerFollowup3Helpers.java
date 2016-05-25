package org.distributedea.agents.systemagents.centralmanager.scheduler;

import jade.core.AID;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.HelpmatesWrapper;
import org.distributedea.ontology.job.Job;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class SchedulerFollowup3Helpers implements Scheduler {

	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	private int MIN_NUMER_OF_METHODS = 3;
	
	public SchedulerFollowup3Helpers() {} // for serialization
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Job job, AgentLogger logger) throws SchedulerException {

		Scheduler schedullerInit = new SchedulerInitialization();
		schedullerInit.agentInitialization(centralManager, job,
				logger);
		
	}

	@Override
	public void replan(Agent_CentralManager centralManager, Job job,
			AgentLogger logger) throws SchedulerException {
		
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
		
		
		//obtain the number of active helpmates
		int numberOfDifferentAD = helpmates.
				exportNumberOfDifferentAgentDescription();
		
		//if number of helpmates is min defined
		if (numberOfDifferentAD <= MIN_NUMER_OF_METHODS) {
			
			//obtain the number of duplicates of minimal prioritized method
			int numberOf = helpmates.exportNumberConcreteAgentDescription(
					minPriorityDescription);
			//if the method is in the system only once
			if (numberOf == 1) {
				return;
			}
		}
				
		
		// agent configurations
		AgentConfiguration bestConfiguration = maxPriorityDescription.getAgentConfiguration();
		AgentConfiguration worstConfiguration = minPriorityDescription.getAgentConfiguration();

		Class<?> bestProblemToolClass = maxPriorityDescription.exportProblemToolClass();
		ProblemStruct problemStruct = job.exportProblemStruct(bestProblemToolClass);
		
		// aid of the worst agent (agent to kill)
		AID worstAID = new AID(worstConfiguration.exportAgentname(), false);
		
		// kill agent with the smallest priority and run the agent with the highest priority
		SchedulerTool.killAndCreateAgent(centralManager, worstAID,
						bestConfiguration, problemStruct, logger);

	}

	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
	}

}
