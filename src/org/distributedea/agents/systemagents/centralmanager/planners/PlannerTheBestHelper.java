package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.helpers.ModelOfHelpmates;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;


public class PlannerTheBestHelper implements IPlanner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IslandModelConfiguration configuration;
	private IAgentLogger logger;
	
	private IPlanner plannerInit = null;

	private int SKIPPED_ITERATIONS_COUNT = 5;
	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;

	public PlannerTheBestHelper() {} // for serialization
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.configuration = configuration;
		this.logger = logger;
		
		plannerInit = new PlannerInitialisationOneMethodPerCore();
		return plannerInit.agentInitialisation(centralManager,
				iteration, jobRun, configuration, logger);		
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
			
		ModelOfHelpmates helpmates = ComputingAgentService.getHelpmates(
				centralManager, NEW_STATISTICS_FOR_EACH_QUERY, logger);
		
		// pring info
		printLog(centralManager, helpmates, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < SKIPPED_ITERATIONS_COUNT) {
			return new InputRePlan(iteration);
		}
		
		InputMethodDescriptions methodsWhichHaveNeverRun =
				history.exportsMethodsWhichHaveNeverRun(jobRun);
		
		if (! methodsWhichHaveNeverRun.isEmpty()) {
		
			InputMethodDescription candidateDescription =
					methodsWhichHaveNeverRun.exportRandomMethodDescription();
			
			Pair<MethodDescription, Integer> minPriorityPair =
					helpmates.exportMinPrioritizedDescription();
			MethodDescription methodToKill = minPriorityPair.first;
			
			return new InputRePlan(iteration, methodToKill, candidateDescription);
		}
		
		Pair<MethodDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		MethodDescription methodToKill = minPriorityPair.first;
		
		Pair<MethodDescription, Integer> maxPriorityPair =
				helpmates.exportMaxPrioritizedDescription();
		MethodDescription maxPriorityDesc = maxPriorityPair.first;
		InputMethodDescription methodToCreate =
				maxPriorityDesc.exportInputMethodDescription();
		
		return new InputRePlan(iteration, methodToKill, methodToCreate).
				exportOptimalizedInpuRePlan();
	}
	
	private void printLog(Agent_CentralManager centralManager,
			ModelOfHelpmates helpmates, IAgentLogger logger) throws Exception {
		
		Pair<MethodDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		MethodDescription minPriorityDescription = minPriorityPair.first;
		int minPriority = minPriorityPair.second;
		
		Pair<MethodDescription, Integer> maxPriorityPair =
				helpmates.exportMaxPrioritizedDescription();
		MethodDescription maxPriorityDescription = maxPriorityPair.first;
		int maxPriority = maxPriorityPair.second;
		
		String minPriorityAgentName = minPriorityDescription.getAgentConfiguration().exportAgentname();
		logger.log(Level.INFO, "The worst: " + minPriorityAgentName + " priority: " + minPriority);

		String maxPriorityAgentName = maxPriorityDescription.getAgentConfiguration().exportAgentname();
		logger.log(Level.INFO, "The best : " + maxPriorityAgentName + " priority: " + maxPriority);
		
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}
	
}
