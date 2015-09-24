package org.distributedea.agents.systemagents.centralmanager;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.problems.ProblemTool;

public interface Scheduler {

	public void agentInitialization(Agent_CentralManager centramManager,
			AgentConfiguration [] configurations, AgentLogger logger);
	
	public void replan(Agent_CentralManager centramManager,
			ProblemTool problemTool, AgentLogger logger);
}