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


public class PlannerInitialisationRandom implements IPlanner {

	private Agent_CentralManager centralManager;
	private IAgentLogger logger;
	private JobRun jobRun;
	private IslandModelConfiguration configuration;
	
	private int globalID = 0;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		this.centralManager = centralManager;
		this.logger = logger;
		this.jobRun = job;
		this.configuration = configuration;
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();
		
		InputMethodDescriptions inputMethodDescr =
				job.getMethods().exportInputMethodDescriptions();
		
		InputPlan inputPlan = new InputPlan(iteration);
		for (AID aidI : managersAID) {
			InputMethodDescription methodDescrI =
					inputMethodDescr.exportRandomMethodDescription();
			inputPlan.add(aidI, methodDescrI.exportPlannedMethodDescription(new MethodIDs(globalID++)));
		}
		
		return PlannerTool.createAndRunAgents(centralManager,
				jobRun, inputPlan, configuration, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history)
			throws Exception {

		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();

		InputMethodDescriptions agentDescriptions =
				jobRun.getMethods().exportInputMethodDescriptions();

		InputMethodDescriptions methodsWhichHaveNeverRun =  history
				.getMethodHistories().exportsMethodsWhichHaveNeverRun(agentDescriptions);

		
		InputPlan inputPlan = new InputPlan(iteration);
		
		for (int i = 0; i < managersAID.size(); i++) {
			AID aidI = managersAID.get(i);
			
			InputMethodDescription methodI = null;
			if (i < methodsWhichHaveNeverRun.size()) {
				methodI = methodsWhichHaveNeverRun.get(i);
			} else {
				methodI = agentDescriptions.exportRandomMethodDescription();
			}
			inputPlan.add(aidI, methodI.exportPlannedMethodDescription(new MethodIDs(globalID++)));
		}
		
		Plan plan = PlannerTool.createAndRunAgents(centralManager,
				jobRun, inputPlan, configuration, logger);
		return new Pair<Plan, RePlan>(plan, new RePlan(iteration));
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
