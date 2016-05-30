package org.distributedea.agents.systemagents.centralmanager.scheduler.initialization;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;

public class SchedulerInitialization implements Scheduler {

	private SchedulerInitializationState state = SchedulerInitializationState.RUN_ONE_AGENT_PER_CORE;
	private boolean methodRepetition = true;
	
	private List<AgentDescription> nextCandidates = null;
	
	public SchedulerInitialization() {
	}
	
	public SchedulerInitialization(SchedulerInitializationState state, boolean methodRepetition) {
		this.state = state;
	}
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws SchedulerException {
				
		NodeInfosWrapper availableNodes = SchedulerTool.getAvailableNodes(centralManager, logger);
		List<AID> managersAID = availableNodes.exportManagerAIDOfEachEmptyCore();
		
		AgentConfigurations configurations = job.getAgentConfigurations();
		List<AgentDescription> descriptions =
				getCartesianProductOfConfigurationsAndTools(configurations, job.getProblemTools());
	
		List<Pair<AID,AgentDescription>> plan = createPlan(managersAID, descriptions);
		
		initializeAndRunAgents(centralManager, job, plan, logger);
	}
	
	
	private List<Pair<AID,AgentDescription>> createPlan(List<AID> managersAID, List<AgentDescription> descriptions) {
	
		if (managersAID.size() == 0) {
			return new ArrayList<Pair<AID,AgentDescription>>();
		}
		
		List<Pair<AID,AgentDescription>> plan = new ArrayList<>();
		
		int numberOfAgents = 0;
		if (state == SchedulerInitializationState.RUN_ONE_AGENT_PER_CORE) {
	
			int numOfConfiguration = descriptions.size();
			int numberOfFreeCores = managersAID.size();
			
			numberOfAgents = Math.min(numberOfFreeCores, numOfConfiguration);
			
			plan = createPairing(managersAID, descriptions, numberOfAgents);
			
			nextCandidates = descriptions.subList(numberOfAgents-1, descriptions.size()-1);
			
			if (methodRepetition) {

				List<AID> managersSupplementAID = 
						managersAID.subList(plan.size()-1, managersAID.size()-1);
				List<Pair<AID,AgentDescription>> planFromRecursion =
						createPlan(managersSupplementAID, descriptions);
				
				List<Pair<AID,AgentDescription>> planRecursive = new ArrayList<>();
				planRecursive.addAll(plan);
				planRecursive.addAll(planFromRecursion);
				return planRecursive;
				
			} else {
				
				return plan;
			}
			
		} else if (state == SchedulerInitializationState.RUN_ALL_COMBINATIONS) {
			
			numberOfAgents = descriptions.size();
			
			plan = createPairing(managersAID, descriptions, numberOfAgents);
			
			return plan;
			
		} else {
			
			return null;
		}
	}
	
	private List<Pair<AID,AgentDescription>> createPairing(List<AID> managersAID,
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
	
	private void initializeAndRunAgents(Agent_CentralManager centralManager, JobRun job,
			List<Pair<AID,AgentDescription>> plan, AgentLogger logger) {
		
		int numberOfDescriotion = plan.size();
		
		List<AID> createdAgents = new ArrayList<>();
		//create computing agents
		for (int cpuIndex = 0; cpuIndex < numberOfDescriotion; cpuIndex++) {	
		
			Pair<AID,AgentDescription> pairI = plan.get(cpuIndex);
			
			AID managerAgentOfEmptyCoreAIDI = pairI.first;
			
			AgentDescription agentDescriptionI = pairI.second;
			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();
			
			AID createdAgentI = ManagerAgentService.sendCreateAgent(centralManager,
					managerAgentOfEmptyCoreAIDI, agentConfigurationI, logger);
			
			createdAgents.add(createdAgentI);
		}
		
		//start computing agent
		for (int cpuIndex = 0; cpuIndex < numberOfDescriotion; cpuIndex++) {

			Pair<AID,AgentDescription> pairI = plan.get(cpuIndex);
			
			AgentDescription agentDescriptionI = pairI.second;
			Class<?> problemToolI = agentDescriptionI.exportProblemToolClass();
			
			ProblemStruct problemStructI = job.exportProblemStruct(problemToolI);
			
			AID createdAgentI = createdAgents.get(cpuIndex);
			
			ComputingAgentService.sendStartComputing(
					centralManager, createdAgentI, problemStructI, logger);
		}
		
	}
	
	/**
	 * Returns candidates for next re-planing
	 * @return
	 */
	public List<AgentDescription> getNextCandidates() {
		return this.nextCandidates;
	}
	
	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			AgentLogger logger) throws SchedulerException {
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
	
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
