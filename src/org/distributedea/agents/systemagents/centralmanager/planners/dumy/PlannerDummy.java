package org.distributedea.agents.systemagents.centralmanager.planners.dumy;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;

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
			Iteration iteration, JobRun job, IAgentLogger logger) throws Exception {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		// chooses node
		AID managerAidI;
		try {
			managerAidI = aidManagerAgents[NODE_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException("Manager agent to create Computing Agent not available");
		}
		

		InputAgentConfigurations configurations = job.getAgentConfigurations();
		
		// chooses agent configuration
		InputAgentConfiguration agentConfiguration;
		try {
			agentConfiguration = configurations.getAgentConfigurations().get(COMPUTING_AGENT_INDEX);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException("Computing agent Configuration not available");
		}
		
		AgentConfiguration createdAgent = ManagerAgentService.sendCreateAgent(
				centralManager, managerAidI, agentConfiguration, logger);
		
		
		
		// chooses ProblemTool
		Class<?> problemToolI = job.getProblemTools().getProblemTools().get(PROBLEM_TOOL_INDEX);
		
		// assumes the existence of only one Computing Agent
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		AID computingAgent = aidComputingAgents[0];
		
		ProblemStruct problemStruct = job.exportProblemStruct(problemToolI);
		
		ComputingAgentService.sendStartComputing(
				centralManager, computingAgent, problemStruct, logger);
		

		MethodDescription createdDescription =
				new MethodDescription(createdAgent, problemToolI);
		
		return new Plan(iteration, createdDescription);
	}

	
	@Override
	public Pair<Plan,RePlan> replan(Iteration iteration, History history
			 ) throws Exception {
		
		Plan plan = new Plan(iteration);
		RePlan rePlan = new RePlan(iteration);
		return new Pair<Plan, RePlan>(plan, rePlan);
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
		
	}


}
