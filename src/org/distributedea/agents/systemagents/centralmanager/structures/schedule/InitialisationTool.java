package org.distributedea.agents.systemagents.centralmanager.structures.schedule;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputPlan;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.javaextension.Pair;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfigurations;
import org.distributedea.ontology.iteration.Iteration;

public class InitialisationTool {

	public static Schedule createSchedule(PlannerInitialisation planner,
			Iteration iteration, List<AID> managersAID,
			List<InputAgentDescription> descriptions) {
	
		if (managersAID == null || managersAID.isEmpty() ||
				descriptions == null || descriptions.isEmpty()) {
			
			InputPlan inputPlan = new InputPlan(iteration, new ArrayList<Pair<AID,InputAgentDescription>>());
						
			return new Schedule(inputPlan, new InputAgentDescriptions(descriptions));
		}

		
		int numberOfAgents = 0;
		if (planner.getState() == PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE) {
	
			int numOfConfiguration = descriptions.size();
			int numberOfFreeCores = managersAID.size();
			
			numberOfAgents = Math.min(numberOfFreeCores, numOfConfiguration);
			
			List<Pair<AID,InputAgentDescription>> planParing =
					createPairing(managersAID, descriptions, numberOfAgents);
			
			List<InputAgentDescription>  nextCandidates = 
					descriptions.subList(numberOfAgents, descriptions.size());
			
			if (planner.isMethodRepetition()) {

				List<AID> managersSupplementAID = 
						managersAID.subList(planParing.size(), managersAID.size());
				Schedule planFromRecursion =
						createSchedule(planner, iteration, managersSupplementAID, descriptions);
				
				List<Pair<AID,InputAgentDescription>> planRecursive = new ArrayList<>();
				planRecursive.addAll(planParing);
				planRecursive.addAll(planFromRecursion.getInputPlan().getSchedule());
				
				InputPlan inputPlan = new InputPlan(iteration, planRecursive);
				return new Schedule(inputPlan,
						new InputAgentDescriptions(nextCandidates));
				
			} else {
				InputPlan inputPlan = new InputPlan(iteration, planParing);
				return new Schedule(inputPlan,
						new InputAgentDescriptions(nextCandidates));
			}
			
		} else if (planner.getState() == PlannerInitialisationState.RUN_ALL_COMBINATIONS) {
			
			numberOfAgents = descriptions.size();
			
			List<Pair<AID,InputAgentDescription>> planParing =
					createPairing(managersAID, descriptions, numberOfAgents);

			return new Schedule(new InputPlan(iteration, planParing));		
			
		} else {
			
			return null;
		}
	}
	
	private static List<Pair<AID,InputAgentDescription>> createPairing(List<AID> managersAID,
			List<InputAgentDescription> descriptions, int numberOfAgents) {
		
		List<Pair<AID,InputAgentDescription>> plan = new ArrayList<>();
		
		// create plan
		for (int agentIndex = 0; agentIndex < numberOfAgents; agentIndex++) {
			
			AID aidI = managersAID.get(agentIndex % managersAID.size());
			InputAgentDescription descriptionI = descriptions.get(agentIndex % descriptions.size());
			
			Pair<AID,InputAgentDescription> pairI = new Pair<>(aidI, descriptionI);
			plan.add(pairI);
		}
		
		return plan;
	}

	public static List<InputAgentDescription> getCartesianProductOfConfigurationsAndTools(
			InputAgentConfigurations configurations, ProblemTools problemTools) {
		
		List<InputAgentDescription> descriptions = new ArrayList<>();
		
		for (InputAgentConfiguration configurationI : configurations.getAgentConfigurations()) {
			
			for (Class<?> problemToolsI : problemTools.getProblemTools()) {
				
				InputAgentDescription descriptionI =
						new InputAgentDescription(configurationI, problemToolsI);
				
				descriptions.add(descriptionI);
			}
		}
		
		return descriptions;
	}
}
