package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;

public class PlannerTheGreatestQMaterialGoodMaterialImprovement implements IPlanner {
	
	private Agent_CentralManager centralManager;
	private IAgentLogger logger;
	private JobRun jobRun;
	private IslandModelConfiguration configuration;
	
	private IPlanner plannerQuantityMaterial;
	private IPlanner plannerGoodMaterial;
	private IPlanner plannerImprovement;
	
	private boolean doMethodSwitch = true;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");

		this.centralManager = centralManager;
		this.logger = logger;
		this.jobRun = jobRun;
		this.configuration = configuration;
		
		this.doMethodSwitch = true;
		
		
		IPlanner plannerInit = new PlannerInitialisationOneMethodPerCore();
		Plan initPlan = plannerInit.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);

		plannerQuantityMaterial = new PlannerTheGreatestQuantityOfMaterial();
		Plan planQuantityMaterial =  plannerQuantityMaterial.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
		initPlan.addAgentsToCreate(planQuantityMaterial.getNewAgents());
		
		plannerGoodMaterial = new PlannerTheGreatestQuantityOfGoodMaterial();
		Plan planGoodMaterial = plannerGoodMaterial.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
		initPlan.addAgentsToCreate(planGoodMaterial.getNewAgents());
		
		plannerImprovement = new PlannerTheGreatestQuantityOfImprovement();
		Plan planImprovement = plannerImprovement.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
		initPlan.addAgentsToCreate(planImprovement.getNewAgents());
		
		return initPlan;
	}

	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history)
			throws Exception {
		
		if (iteration.getIterationNumber() <
				iteration.getExpectetMaxIterationNumber() / 3) {
			
			return plannerQuantityMaterial.replan(iteration, history);
			
		} else if (iteration.getIterationNumber() <
				iteration.getExpectetMaxIterationNumber() * 2 / 3) {
			
			if (doMethodSwitch) {
				doMethodSwitch = false;
				
				InputRePlan inputRePlan = swapMethods(iteration, history);
				RePlan rePlan = PlannerTool.processReplanning(centralManager,
						inputRePlan, jobRun, configuration, logger);
				
				return new Pair<Plan, RePlan>(new Plan(iteration), rePlan);
			}
			
			return plannerGoodMaterial.replan(iteration, history);
			
		} else {
			
			return plannerImprovement.replan(iteration, history);
		}
		
	}

	/**
	 * Swap running methods for not running method
	 * @param iteration
	 * @param history
	 * @return
	 */
	private InputRePlan swapMethods(Iteration iteration, History history) {

		MethodDescriptions runningMethods =
				history.exportRunningMethods();

		Methods runningInputMethods =
				runningMethods.exportInputAgentDescriptions();
		
		Methods allMethods =
				jobRun.getMethods().exportInputMethodDescriptions();
		Methods deadMethods =
				allMethods.exportComplement(runningInputMethods);
		
		InputRePlan inputRePlan = new InputRePlan(iteration);
		
		int methodToSwitchNumber =
				Math.min(runningInputMethods.size(), deadMethods.size());
		
		for (int i = 0; i < methodToSwitchNumber; i++) {
			
			MethodDescription runningI = runningMethods.export(i);
			InputMethodDescription deadI = deadMethods.get(i);
			
			inputRePlan.addAgentsToKill(runningI);
			inputRePlan.addAgentsToCreate(deadI);
		}
		
		return inputRePlan;
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
