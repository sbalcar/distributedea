package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRandom;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistories;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;


public class PlannerRandom implements IPlanner {
	
	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IslandModelConfiguration configuration;
	private IAgentLogger logger;
	
	private IPlanner plannerInit = null;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.configuration = configuration;
		this.logger = logger;
		
		
		plannerInit = new PlannerInitialisationRandom();
		return plannerInit.agentInitialisation(centralManager, iteration,
				jobRun, configuration, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws Exception {		

		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);

		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, configuration, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
			 ) throws Exception {

		MethodHistories currentMethodsHistory = history.getMethodHistories()
				.exportHistoryOfRunningMethods(iteration, 0);

		//random select agent to kill
		MethodDescription methodToKill =
				currentMethodsHistory.exportRandomRunningMethod();
		
		//random select agent to create
		InputMethodDescription methodToCreate =
				jobRun.getMethods().exportRandomMethodDescription();
		
		return new InputRePlan(iteration, methodToKill, methodToCreate);
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}
}
