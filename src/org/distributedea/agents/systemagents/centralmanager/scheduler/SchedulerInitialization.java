package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problem.Problem;

public class SchedulerInitialization implements Scheduler {

	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Problem problem, List<AgentConfiguration> configurations,
			List<Class<?>> availablProblemTools, AgentLogger logger)
			throws SchedulerException {
		
		List<AgentDescription> descriptions =
				getCartesianProductOfConfigurationsAndTools(configurations, availablProblemTools);
		
	}

	@Override
	public boolean continueWithComputingInTheNextGeneration() {
		return true;
	}

	@Override
	public void replan(Agent_CentralManager centralManager, Problem problem,
			List<AgentConfiguration> configurations,
			List<Class<?>> availableProblemTools, AgentLogger logger)
			throws SchedulerException {
	}

	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
	
	}

	
	public static List<AgentDescription> getCartesianProductOfConfigurationsAndTools(
			List<AgentConfiguration> configurations, List<Class<?>> problemTools) {
		
		List<AgentDescription> descriptions = new ArrayList<AgentDescription>();
		
		for (AgentConfiguration configurationI : configurations) {
			
			for (Class<?> problemToolsI : problemTools) {
				
				AgentDescription descriptionI = new AgentDescription();
				descriptionI.setAgentConfiguration(configurationI);
				descriptionI.setProblemToolClass(problemToolsI.getName());
				
				descriptions.add(descriptionI);
			}
		}
		
		return descriptions;
	}
	
}
