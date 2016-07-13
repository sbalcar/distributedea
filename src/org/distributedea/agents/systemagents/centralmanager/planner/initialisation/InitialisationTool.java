package org.distributedea.agents.systemagents.centralmanager.planner.initialisation;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;

public class InitialisationTool {

	public static Schedule createPlan(PlannerInitialisation scheduler, List<AID> managersAID, List<AgentDescription> descriptions) {
	
		if (managersAID == null || managersAID.isEmpty() ||
				descriptions == null || descriptions.isEmpty()) {
			
			Schedule plan = new Schedule();
			plan.setPlan(new ArrayList<Pair<AID,AgentDescription>>());
			plan.setNextCandidates(descriptions);
			
			return plan;
		}
		
		int numberOfAgents = 0;
		if (scheduler.getState() == PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE) {
	
			int numOfConfiguration = descriptions.size();
			int numberOfFreeCores = managersAID.size();
			
			numberOfAgents = Math.min(numberOfFreeCores, numOfConfiguration);
			
			List<Pair<AID,AgentDescription>> planParing =
					createPairing(managersAID, descriptions, numberOfAgents);
			
			List<AgentDescription>  nextCandidates = 
					descriptions.subList(numberOfAgents, descriptions.size());
			
			if (scheduler.isMethodRepetition()) {

				List<AID> managersSupplementAID = 
						managersAID.subList(planParing.size(), managersAID.size());
				Schedule planFromRecursion =
						createPlan(scheduler, managersSupplementAID, descriptions);
				
				List<Pair<AID,AgentDescription>> planRecursive = new ArrayList<>();
				planRecursive.addAll(planParing);
				planRecursive.addAll(planFromRecursion.getSchedule());
				
				return new Schedule(planRecursive, nextCandidates);
				
			} else {
				
				return new Schedule(planParing, nextCandidates);
			}
			
		} else if (scheduler.getState() == PlannerInitialisationState.RUN_ALL_COMBINATIONS) {
			
			numberOfAgents = descriptions.size();
			
			List<Pair<AID,AgentDescription>> planParing =
					createPairing(managersAID, descriptions, numberOfAgents);
			
			return new Schedule(planParing, null);
			
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
