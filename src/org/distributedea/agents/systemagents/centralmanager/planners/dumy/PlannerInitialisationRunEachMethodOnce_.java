package org.distributedea.agents.systemagents.centralmanager.planners.dumy;


import java.util.ArrayList;
import java.util.List;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;

public class PlannerInitialisationRunEachMethodOnce_ implements IPlanner {

	int NODE_INDEX = 0;
	
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
		
		// chooses ProblemTool by index
		Class<?> problemToolI;
		try {
			problemToolI = job.getProblemTools().getProblemTools().get(PROBLEM_TOOL_INDEX);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalStateException("ProblemTool not available");
		}
		
		
		List<MethodDescription> createdDescriptions = new ArrayList<>();
		
		// create one agent for each configuration
		InputAgentConfigurations configurations = job.getAgentConfigurations();
		for (InputAgentConfiguration agentConfigurationI : configurations.getAgentConfigurations()) {
			
			AgentConfiguration createdAgentI = ManagerAgentService.sendCreateAgent(
					centralManager, managerAidI, agentConfigurationI, logger);
			
			MethodDescription descriptionI = new MethodDescription(
					createdAgentI, job.getProblemDefinition(), problemToolI);
						
			createdDescriptions.add(descriptionI);
		}
				
		
		// search all Computing Agents
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		// start computing in all created computing agents
		for (AID aidComputingAgentI : aidComputingAgents) {
			
			ProblemStruct problemStructI = job.exportProblemStruct(problemToolI);
						
			ComputingAgentService.sendStartComputing(
					centralManager, aidComputingAgentI, problemStructI, logger);

		}
		
		return new Plan(iteration, new MethodDescriptions(createdDescriptions));
	}

	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history) {
		
		Plan plan = new Plan(iteration);
		RePlan rePlan = new RePlan(iteration);
		return new Pair<>(plan, rePlan);
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
		
	}
	
}
