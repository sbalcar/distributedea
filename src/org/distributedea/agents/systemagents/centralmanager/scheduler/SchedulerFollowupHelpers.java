package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jade.core.AID;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptionWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.HelpmateList;
import org.distributedea.ontology.problem.Problem;

public class SchedulerFollowupHelpers implements Scheduler {

	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	
	private int numberOfReplaning = 0;
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, List<AgentConfiguration> configurations,
			List<Class<?>> availablProblemTools, AgentLogger logger) throws SchedulerException {
		
		SchedulerRunEachMethodOnce scheduler = new SchedulerRunEachMethodOnce();
		scheduler.agentInitialization(centralManager, problem, configurations,
				availablProblemTools, logger);
		
	}

	@Override
	public void replan(Agent_CentralManager centralManager, Problem problem,
			List<AgentConfiguration> configurations,
			List<Class<?>> availableProblemTools, AgentLogger logger) throws SchedulerException {
		
		numberOfReplaning++;
		
		// search all Computing Agents
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		List<HelpmateList> helpers = new ArrayList<HelpmateList>();
		
		// going through all computing agents
		for (AID aidComputingAgentI : aidComputingAgents) {
			
			HelpmateList helpmateListI = 
					ComputingAgentService.sendReportHelpmate(
							centralManager, aidComputingAgentI,
							NEW_STATISTICS_FOR_EACH_QUERY, logger);
			helpers.add(helpmateListI);
		}
		

		// initialization of data structure
		Map<AgentDescription, Integer> helpmateMapI = new HashMap<AgentDescription, Integer>();
		for (HelpmateList helpmateListI: helpers) {
			helpmateMapI.put(helpmateListI.getDescription(), 0);
		}
		
		// count priority in global
		for (HelpmateList helpmateListI: helpers) {
			List<AgentDescriptionWrapper> wrappers = helpmateListI.getDescriptions();

			// going through all results regarding their helpers
			for (AgentDescriptionWrapper wrapperI : wrappers) {
				
				AgentDescription descriptionI = wrapperI.getDescription();
				int priorityI = wrapperI.getPriority();
				
				if (helpmateMapI.containsKey(descriptionI)) {
					int freqeuency = helpmateMapI.get(descriptionI);
					helpmateMapI.put(descriptionI, freqeuency+priorityI);
				} else {
					// throws out information about agent which still doesn't exist
				}
			}
			
		}
		
		
		int minPriority = Integer.MAX_VALUE;
		AgentDescription minPriorityDescription = null;

		int maxPriority = Integer.MIN_VALUE;
		AgentDescription maxPriorityDescription = null;
		
		for (Map.Entry<AgentDescription, Integer> entry : helpmateMapI.entrySet()) {
			
			AgentDescription descriptionI = entry.getKey();
		    int priorityI = entry.getValue();
		    
		    if (priorityI < minPriority) {
		    	minPriority = priorityI;
		    	minPriorityDescription = descriptionI;
		    }
		    if (priorityI > maxPriority) {
		    	maxPriority = priorityI;
		    	maxPriorityDescription = descriptionI;
		    }		    
		}
		
		String minPriorityAgentName = minPriorityDescription.getAgentConfiguration().getAgentName();
		System.out.println("The worst: " + minPriorityAgentName + " priority: " + minPriority);

		String maxPriorityAgentName = maxPriorityDescription.getAgentConfiguration().getAgentName();
		System.out.println("The best : " + maxPriorityAgentName + " priority: " + maxPriority);
		
		
		AgentConfiguration bestConfiguration = maxPriorityDescription.getAgentConfiguration();
		bestConfiguration.editAgentName();
		AID worstAID = new AID(minPriorityAgentName, false);
		
		// kill agent with the smallest priority and run the agent with the highest priority
		try {
			SchedulerTool.killAndCreateAgent(centralManager, worstAID,
						bestConfiguration, problem, logger);
		} catch (SchedulerException e) {
			logger.logThrowable("Error by replacing agent", e);
			throw new SchedulerException("");
		}
	}

	@Override
	public boolean continueWithComputingInTheNextGeneration() {
		return numberOfReplaning < InputConfiguration.numberOfReplanning;
	}

	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
	}
	
}
