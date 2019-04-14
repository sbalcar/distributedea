package org.distributedea.agents.systemagents.centralmanager.planners.onlyinit;

import jade.core.AID;

import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputPlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;


public class PlannerInitialisationRunEachMethodOnce implements IPlanner {

	private int globalID = 0;

	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun,
			IslandModelConfiguration configuration, IAgentLogger logger)
			throws Exception {
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();

		InputMethodDescriptions agentDescriptions =
				jobRun.getMethods().exportInputMethodDescriptions();
		
		
		InputPlan inputPlan = new InputPlan(iteration);
		
		for (int i = 0; i < agentDescriptions.size(); i++) {

			InputMethodDescription inputMethodDescrI =
					agentDescriptions.get(i);

			AID aidManagerI = managersAID.get(i % managersAID.size());
			
			inputPlan.add(aidManagerI, inputMethodDescrI.exportPlannedMethodDescription(new MethodIDs(globalID++)));
		}
		
		return PlannerTool.createAndRunAgents(centralManager,
				jobRun, inputPlan, configuration, logger);
	}

	public Plan agentInitialisationOnlyCreateAgents(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws Exception {
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();

		InputMethodDescriptions agentDescriptions =
				jobRun.getMethods().exportInputMethodDescriptions();
		
		
		InputPlan inputPlan = new InputPlan(iteration);
		
		for (int i = 0; i < agentDescriptions.size(); i++) {

			InputMethodDescription iAgentDescriptionI =
					agentDescriptions.get(i);

			AID aidManagerI = managersAID.get(i % managersAID.size());
			
			inputPlan.add(aidManagerI, iAgentDescriptionI.exportPlannedMethodDescription(new MethodIDs(globalID++)));
		}
		
		return PlannerTool.createAgents(centralManager,
				inputPlan, jobRun.getProblem(), logger);
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
