package org.distributedea.agents.systemagents.centralmanager;

import jade.core.AID;

import java.util.List;

import org.distributedea.agents.computingagents.computingagent.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.management.agent.Argument;
import org.distributedea.ontology.management.computingnode.NodeInfo;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.permutation.ProblemTool2opt;
import org.distributedea.problems.tsp.permutation.ProblemToolSimpleSwap;

public class Scheduler1 implements Scheduler {

	public void agentInitialization(Agent_CentralManager centramManager,
			AgentConfiguration [] configurations, AgentLogger logger) {
	
		AID [] aidManagerAgents = centramManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		for (AID managerAidI : aidManagerAgents) {	
			
			NodeInfo nodeInfoI = ManagerAgentService.sendNodeInfo(
					centramManager, managerAidI, logger);
			
			int numberOfCPU = nodeInfoI.getNumberCPU();
			
			for (int cpuI = 0; cpuI < numberOfCPU; cpuI++) {
				
				AgentConfiguration agentConfiguration = configurations[2];
				String agentType = agentConfiguration.getAgentType();
				String agentName = agentConfiguration.getAgentName();
				List<Argument> arguments = agentConfiguration.getArguments();
				
				ManagerAgentService.sendCreateAgent(centramManager,
						managerAidI, agentType, agentName, arguments, logger);
			}
		}
		
	}

	@Override
	public void replan(Agent_CentralManager centralManager, ProblemTool problemTool, AgentLogger logger) {
		
		//AID computingAgent = new AID("Agent_HillClimbing-17", false);
		//AID computingAgent = new AID("Agent_RandomSearch-17", false);
		AID computingAgent = new AID("Agent_Evolution-17", false);
		
		//ResultOfComputing resultOfComputing =
		//		ComputingAgentService.sendAccessesResult(centralManager, computingAgent, logger);


		String problemFileName = org.distributedea.Configuration.getInputFile("it16862.tsp");
		
		Problem problem = problemTool.readProblem(problemFileName, logger);
		problem.setProblemToolClass(ProblemTool2opt.class.getName());
		
		ComputingAgentService.sendStartComputing(centralManager, computingAgent, problem, logger);
		
	}
	
}
