package org.distributedea.agents.systemagents.centralmanager.planner;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

/**
 * Dummy Scheduler which runs on one node
 * one Computing Agent with one ProblemTool
 *
 */
public class PlannerDummy implements Planner {
	
	int NODE_INDEX = 0;
	
	/** index on the method in the list of methods (configuration/methods.xml) */
	int COMPUTING_AGENT_INDEX = 1;
	
	/** index on the Problem Tool in the list of tolls */
	int PROBLEM_TOOL_INDEX = 0;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job, IAgentLogger logger) throws PlannerException {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		// chooses node
		AID managerAidI;
		try {
			managerAidI = aidManagerAgents[NODE_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new PlannerException("Manager agent to create Computing Agent not available");
		}
		

		AgentConfigurations configurations = job.getAgentConfigurations();
		
		// chooses agent configuration
		AgentConfiguration agentConfiguration;
		try {
			agentConfiguration = configurations.getAgentConfigurations().get(COMPUTING_AGENT_INDEX);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new PlannerException("Computing agent Configuration not available");
		}
		
		AgentConfiguration createdAgent = ManagerAgentService.sendCreateAgent(centralManager,
				managerAidI, agentConfiguration, logger);
		
		
		
		// chooses ProblemTool
		Class<?> problemToolI = job.getProblemTools().getProblemTools().get(PROBLEM_TOOL_INDEX);
		
		// assumes the existence of only one Computing Agent
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		AID computingAgent = aidComputingAgents[0];
		
		ProblemStruct problemStruct = job.exportProblemStruct(problemToolI);
		
		ComputingAgentService.sendStartComputing(
				centralManager, computingAgent, problemStruct, logger);
		

		AgentDescription createdDescription = new AgentDescription();
		createdDescription.setAgentConfiguration(createdAgent);
		createdDescription.importProblemToolClass(problemToolI);
		
		return new Plan(iteration);
	}

	
	@Override
	public Pair<Plan,RePlan> replan(Iteration iteration, History history
			 ) throws PlannerException {
		Plan plan = new Plan(iteration);
		RePlan rePlan = new RePlan(iteration);
		return new Pair<Plan, RePlan>(plan, rePlan);
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}


}
