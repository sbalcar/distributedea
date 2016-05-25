package org.distributedea.agents.systemagents.centralmanager.scheduler;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.Job;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;

public class SchedulerInitialization implements Scheduler {

	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Job job, AgentLogger logger) throws SchedulerException {
		
		AgentConfigurations configurations = job.getAgentConfigurations();
		List<AgentDescription> descriptions =
				getCartesianProductOfConfigurationsAndTools(configurations, job.getProblemTools());
		int numberOfDescriotion = descriptions.size();
		
		NodeInfosWrapper availableNodes = SchedulerTool.getAvailableNodes(centralManager, logger);
		int numberOfCPU = availableNodes.exportNumberOfCores();
		List<AID> aids = availableNodes.exportManagersAID();
		
		int e = Math.max(numberOfCPU, numberOfDescriotion);
		
		for (int cpuIndex = 0; cpuIndex < e; cpuIndex++) {	
		
			AgentDescription agentDescriptionI = descriptions.get(cpuIndex % numberOfDescriotion);
			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();
			Class<?> problemToolI = agentDescriptionI.exportProblemToolClass();
			
			AID managerAgentOfEmptyCoreAIDI = aids.get(cpuIndex % aids.size());
			
			AID createdAgentI = ManagerAgentService.sendCreateAgent(centralManager,
					managerAgentOfEmptyCoreAIDI, agentConfigurationI, logger);
			
			
			ProblemStruct problemStructI = job.exportProblemStruct(problemToolI);
			
			ComputingAgentService.sendStartComputing(
					centralManager, createdAgentI, problemStructI, logger);
		}
		
	}
	
	@Override
	public void replan(Agent_CentralManager centralManager, Job job,
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
