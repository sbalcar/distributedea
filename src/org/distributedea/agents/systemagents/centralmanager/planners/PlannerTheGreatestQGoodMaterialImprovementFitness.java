package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;

public class PlannerTheGreatestQGoodMaterialImprovementFitness implements IPlanner {
	
	private IPlanner plannerGoodMaterial;
	private IPlanner plannerImprovement;
	private IPlanner plannerFitAverage;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		
		IPlanner plannerInit = new PlannerInitialisationOneMethodPerCore();
		Plan initPlan = plannerInit.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
		
		plannerGoodMaterial = new PlannerTheGreatestQuantityOfGoodMaterial();
		Plan planGoodMaterial = plannerGoodMaterial.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
		initPlan.addAgentsToCreate(planGoodMaterial.getNewAgents());
		
		plannerImprovement = new PlannerTheGreatestQuantityOfImprovement();
		Plan planImprovement = plannerImprovement.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
		initPlan.addAgentsToCreate(planImprovement.getNewAgents());
		
		plannerFitAverage = new PlannerTheBestAverageOfFitness();
		Plan planFitAverage =  plannerFitAverage.agentInitialisation(centralManager, iteration, jobRun, configuration, logger);
		initPlan.addAgentsToCreate(planFitAverage.getNewAgents());
		
		return initPlan;
	}
	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history)
			throws Exception {
		
		if (iteration.getIterationNumber() <
				iteration.getExpectetMaxIterationNumber() / 3) {
			
			return plannerGoodMaterial.replan(iteration, history);
			
		} else if (iteration.getIterationNumber() <
				iteration.getExpectetMaxIterationNumber() * 2 / 3) {
	
			return plannerImprovement.replan(iteration, history);
			
		} else {
			
			return plannerFitAverage.replan(iteration, history);
		}
		
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
