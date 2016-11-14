package org.distributedea.agents.systemagents.centralmanager.planners.onlyinit;

import jade.core.AID;

import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputPlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;


public class PlannerInitialisationOneMethodPerCore implements Planner {

	private Agent_CentralManager centralManager;
	private IAgentLogger logger;
	
	private JobRun jobRun;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger)
			throws Exception {
		
		this.centralManager = centralManager;
		this.logger = logger;
		
		this.jobRun = jobRun;
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();

		InputMethodDescriptions agentDescriptions =
				jobRun.exportInputAgentDescriptions();
		
		
		InputPlan inputPlan = new InputPlan(iteration);
		
		for (int i = 0; i < managersAID.size(); i++) {

			AID aidManagerI = managersAID.get(i);
			
			InputMethodDescription iAgentDescriptionI =
					agentDescriptions.get(i % agentDescriptions.size());
			
			inputPlan.add(aidManagerI, iAgentDescriptionI);
		}
		
		return PlannerTool.createAndRunAgents(centralManager,
				jobRun, inputPlan, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history) {
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();
		
		InputMethodDescriptions methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
		
		InputPlan inputPlan = new InputPlan(iteration);
		
		int i = 0;
		while (i < Math.min(managersAID.size(),
				methodsWhichHaveNeverRun.size())) {
			
			AID aidManagerI = managersAID.get(i);
			
			InputMethodDescription methodNeverRunI =
					methodsWhichHaveNeverRun.get(i);
			
			inputPlan.add(aidManagerI, methodNeverRunI);
			
			i++;
		}
	
		while (i < managersAID.size()) {
			
			AID aidManagerI = managersAID.get(i);
			
			InputMethodDescription iAgentDescriptionI =
					jobRun.exportInputAgentDescriptions().
					exportRandomInputAgentDescription();
			
			inputPlan.add(aidManagerI, iAgentDescriptionI);
			
			i++;
		}
		
		Plan plan = PlannerTool.createAndRunAgents(centralManager,
				jobRun, inputPlan, logger);
		
		RePlan rePlan = new RePlan(iteration);
		return new Pair<>(plan, rePlan);
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
