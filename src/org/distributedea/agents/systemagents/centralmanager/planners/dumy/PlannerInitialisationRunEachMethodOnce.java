package org.distributedea.agents.systemagents.centralmanager.planners.dumy;


import java.util.ArrayList;
import java.util.List;

import jade.core.AID;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;

public class PlannerInitialisationRunEachMethodOnce implements IPlanner {

	
	int NODE_INDEX = 0;
		
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		// chooses node
		AID managerAidI;
		try {
			managerAidI = aidManagerAgents[NODE_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException("Manager agent to create Computing Agent not available");
		}
		
		Methods methods = job.getMethods();
		IProblem problem = job.getProblem();
		
		
		List<Pair<InputMethodDescription, ProblemWrapper>> metdodsToCreate = job.exportProblemWrappers();
		
		List<MethodDescription> methodsCreated = new ArrayList<>();
		
		
		for (int i = 0; i < methods.size(); i++) {
			
			Pair<InputMethodDescription, ProblemWrapper> problemWrapI = job.exportProblemWrapper(i);
			
			InputMethodDescription inputMethodDescrI = problemWrapI.first;
			
			InputAgentConfiguration inputAgentConfI = inputMethodDescrI.getInputAgentConfiguration();
			ProblemToolDefinition problemToolDef = inputMethodDescrI.getProblemToolDefinition();
			
			AgentConfiguration createdAgentI = ManagerAgentService.sendCreateAgent(
					centralManager, managerAidI, inputAgentConfI, logger);
						
			methodsCreated.add(new MethodDescription(
					createdAgentI, problem, problemToolDef));
		}

		for (int i = 0; i < methodsCreated.size(); i++) {
			
			MethodDescription methodDesrI  = methodsCreated.get(i);
			AgentConfiguration createdAgentConfI = methodDesrI.getAgentConfiguration();
			Pair<InputMethodDescription, ProblemWrapper> pairI = metdodsToCreate.get(i);
			
			ProblemWrapper problemWrpI = pairI.second;
			
			AID aidComputingAgentI = createdAgentConfI.exportAgentAID();
			
			boolean startOK = ComputingAgentService.sendStartComputing( centralManager,
					aidComputingAgentI, problemWrpI, configuration, logger);
			if (! startOK) {
				centralManager.exit();
			}
		}
		
		return new Plan(iteration, new MethodDescriptions(methodsCreated));
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
