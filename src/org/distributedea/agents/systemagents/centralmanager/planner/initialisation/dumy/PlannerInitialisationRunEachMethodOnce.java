package org.distributedea.agents.systemagents.centralmanager.planner.initialisation.dumy;


import java.util.ArrayList;
import java.util.List;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
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

public class PlannerInitialisationRunEachMethodOnce implements Planner {

	int NODE_INDEX = 0;
	
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
		
		// chooses ProblemTool by index
		Class<?> problemToolI = job.getProblemTools().getProblemTools().get(PROBLEM_TOOL_INDEX);
		
		List<AgentDescription> createdDescriptions = new ArrayList<>();
		
		// create one agent for each configuration
		AgentConfigurations configurations = job.getAgentConfigurations();
		for (AgentConfiguration agentConfigurationI : configurations.getAgentConfigurations()) {
			
			AgentConfiguration createdAgentI = ManagerAgentService.sendCreateAgent(
					centralManager, managerAidI, agentConfigurationI, logger);
			
			AgentDescription descriptionI = new AgentDescription();
			descriptionI.setAgentConfiguration(createdAgentI);
			descriptionI.importProblemToolClass(problemToolI);
			
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
		
		return new Plan(iteration, createdDescriptions);
	}

	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history) {
		
		Plan plan = new Plan(iteration, new ArrayList<AgentDescription>());
		RePlan rePlan = new RePlan(iteration);
		return new Pair<>(plan, rePlan);
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}
	
}
