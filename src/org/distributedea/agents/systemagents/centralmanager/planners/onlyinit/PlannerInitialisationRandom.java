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
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;


public class PlannerInitialisationRandom implements Planner {

	private Agent_CentralManager centralManager;
	private IAgentLogger logger;
	private JobRun jobRun;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job, IAgentLogger logger)
			throws Exception {
		
		this.centralManager = centralManager;
		this.logger = logger;
		this.jobRun = job;
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();
		
		InputAgentDescriptions agentDescriptions =
				job.exportInputAgentDescriptions();
		
		InputPlan inputPlan = new InputPlan(iteration);
		for (AID aidI : managersAID) {
			InputAgentDescription agentDescriptionI =
					agentDescriptions.exportRandomInputAgentDescription();
			inputPlan.add(aidI, agentDescriptionI);
		}
		
		return PlannerTool.createAndRunAgents(centralManager,
				jobRun, inputPlan, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history)
			throws Exception {

		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();

		InputAgentDescriptions agentDescriptions =
				jobRun.exportInputAgentDescriptions();

		InputAgentDescriptions methodsWhichHaveNeverRun =  history
				.getMethodHistories().exportsMethodsWhichHaveNeverRun(agentDescriptions);

		
		InputPlan inputPlan = new InputPlan(iteration);
		
		for (int i = 0; i < managersAID.size(); i++) {
			AID aidI = managersAID.get(i);
			
			if (i < methodsWhichHaveNeverRun.size()) {
				InputAgentDescription methodNeverRunI = 
						methodsWhichHaveNeverRun.get(i);
				inputPlan.add(aidI, methodNeverRunI);
			} else {
				InputAgentDescription methodRandomI = agentDescriptions
						.exportRandomInputAgentDescription();
				inputPlan.add(aidI, methodRandomI);
			}
		}
		
		Plan plan = PlannerTool.createAndRunAgents(centralManager,
				jobRun, inputPlan, logger);
		return new Pair<Plan, RePlan>(plan, new RePlan(iteration));
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
