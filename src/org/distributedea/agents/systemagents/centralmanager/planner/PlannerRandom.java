package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel.ResultsOfComputing;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodinstancedescription.MethodsResults;
import org.distributedea.ontology.monitor.MethodStatistic;

public class PlannerRandom implements Planner {
	
	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	Random ran = new Random();
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws PlannerException {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		
		plannerInit = new PlannerInitialisation(state, true);
		return plannerInit.agentInitialisation(centralManager, iteration, jobRun, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws PlannerException {		

		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);

		// process RePlan
		RePlan rePlan = replanning(iteration, history);
		PlannerTool.processReplanning(centralManager, rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlan);
	}
	
	private RePlan replanning(Iteration iteration, History history
			 ) throws PlannerException {
				
		History historyOfCurrentMethods = history.
				exportHistoryOfRunningMethods(iteration, 1);
		
		ResultsOfComputing resultOfComputingWrapper =
				ComputingAgentService.sendAccessesResult_(centralManager, logger);
		
		AgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			return PlannerFollowBestResult.killWorstAndReplaceByNewMethod(
					iteration, jobRun, resultOfComputingWrapper, candidateDescription);
		}
	
		return replanRandomByRandomMethod(iteration, historyOfCurrentMethods);
	}
	
	
	RePlan replanRandomByRandomMethod(Iteration iteration,
			History historyOfCurrentMethods) throws PlannerException {			

		//random select agent to kill
		MethodsResults lastStatistic = historyOfCurrentMethods
				.exportMethodsResults(iteration);
		
		MethodStatistic randomMethodStatistic =
				lastStatistic.exportRandomMethodStatistic();
		if (randomMethodStatistic == null) {
			return new RePlan(iteration);
		}
		AgentDescription methodToKill =
				randomMethodStatistic.getAgentDescription();
		
		
		//random select configuration for creating the new agent
		AgentConfigurations configurations = jobRun.getAgentConfigurations();
		
		//random select problem tool for the new computation
		List<Class<?>> problemTools = jobRun.getProblemTools().getProblemTools();

		
		AgentDescription methodToCreate =
				chooseRandomMethod(configurations, problemTools);
		
		return new RePlan(iteration, methodToKill,
				methodToCreate);
	}
	
	private AgentDescription chooseRandomMethod(
			AgentConfigurations configurations, List<Class<?>> problemTools) {
		
		int indexAC = ran.nextInt(configurations.getAgentConfigurations().size());
		AgentConfiguration agentToCreate = configurations.exportAgentConfigurations(indexAC);
		
		int indexPT = ran.nextInt(problemTools.size());
		Class<?> problemToolClass = problemTools.get(indexPT);
		
		AgentDescription methodToCreate = new AgentDescription();
		methodToCreate.setAgentConfiguration(agentToCreate);
		methodToCreate.importProblemToolClass(problemToolClass);
		
		return methodToCreate;
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}
}
