package org.distributedea.agents.systemagents.centralmanager.scheduler.initialization;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.Pair;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;

public class InitializationTool {

	public static Plan createPlan(SchedulerInitialization scheduler, List<AID> managersAID, List<AgentDescription> descriptions) {
	
		if (managersAID == null || managersAID.isEmpty() ||
				descriptions == null || descriptions.isEmpty()) {
			
			Plan plan = new Plan();
			plan.setPlan(new ArrayList<Pair<AID,AgentDescription>>());
			plan.setNextCandidates(descriptions);
			
			return plan;
		}
		
		int numberOfAgents = 0;
		if (scheduler.getState() == SchedulerInitializationState.RUN_ONE_AGENT_PER_CORE) {
	
			int numOfConfiguration = descriptions.size();
			int numberOfFreeCores = managersAID.size();
			
			numberOfAgents = Math.min(numberOfFreeCores, numOfConfiguration);
			
			List<Pair<AID,AgentDescription>> planParing =
					createPairing(managersAID, descriptions, numberOfAgents);
			
			List<AgentDescription>  nextCandidates = 
					descriptions.subList(numberOfAgents-1, descriptions.size()-1);
			
			if (scheduler.isMethodRepetition()) {

				List<AID> managersSupplementAID = 
						managersAID.subList(planParing.size()-1, managersAID.size()-1);
				Plan planFromRecursion =
						createPlan(scheduler, managersSupplementAID, descriptions);
				
				List<Pair<AID,AgentDescription>> planRecursive = new ArrayList<>();
				planRecursive.addAll(planParing);
				planRecursive.addAll(planFromRecursion.getPlan());
				
				return new Plan(planRecursive, nextCandidates);
				
			} else {
				
				return new Plan(planParing, nextCandidates);
			}
			
		} else if (scheduler.getState() == SchedulerInitializationState.RUN_ALL_COMBINATIONS) {
			
			numberOfAgents = descriptions.size();
			
			List<Pair<AID,AgentDescription>> planParing =
					createPairing(managersAID, descriptions, numberOfAgents);
			
			return new Plan(planParing, null);
			
		} else {
			
			return null;
		}
	}
	
	private static List<Pair<AID,AgentDescription>> createPairing(List<AID> managersAID,
			List<AgentDescription> descriptions, int numberOfAgents) {
		
		List<Pair<AID,AgentDescription>> plan = new ArrayList<>();
		
		// create plan
		for (int agentIndex = 0; agentIndex < numberOfAgents; agentIndex++) {
			
			AID aidI = managersAID.get(agentIndex % managersAID.size());
			AgentDescription descriptionI = descriptions.get(agentIndex % descriptions.size());
			
			Pair<AID,AgentDescription> pairI = new Pair<>(aidI, descriptionI);
			plan.add(pairI);
		}
		
		return plan;
	}

	public static List<AgentDescription> getCartesianProductOfConfigurationsAndTools(
			AgentConfigurations configurations, ProblemTools problemTools) {
		
		List<AgentDescription> descriptions = new ArrayList<AgentDescription>();
		
		for (AgentConfiguration configurationI : configurations.getAgentConfigurations()) {
			
			for (Class<?> problemToolsI : problemTools.getProblemTools()) {
				
				AgentDescription descriptionI = new AgentDescription();
				descriptionI.setAgentConfiguration(configurationI);
				descriptionI.setProblemToolClass(problemToolsI.getName());
				
				descriptions.add(descriptionI);
			}
		}
		
		return descriptions;
	}
}
