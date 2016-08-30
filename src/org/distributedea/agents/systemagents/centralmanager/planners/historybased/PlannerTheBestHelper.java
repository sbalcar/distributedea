package org.distributedea.agents.systemagents.centralmanager.planners.historybased;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.helpers.ModelOfHelpmates;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;

public class PlannerTheBestHelper implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	
	public PlannerTheBestHelper() {} // for serialization
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws Exception {
		
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
			 ) throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
			 ) throws Exception {
			
		ModelOfHelpmates helpmates = ComputingAgentService.getHelpmates(
				centralManager, NEW_STATISTICS_FOR_EACH_QUERY, logger);
		
		// pring info
		printLog(centralManager, helpmates, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return new InputRePlan(iteration);
		}
		
		InputAgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			return killWorstAndReplaceByNewMethod(jobRun, iteration, helpmates,
					candidateDescription);
		}
		
		return killWorstAndDuplicateBestHelpmate(jobRun,
				iteration, helpmates);

	}
	
	static void printLog(Agent_CentralManager centralManager,
			ModelOfHelpmates helpmates, IAgentLogger logger) throws Exception {
		
		Pair<AgentDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		AgentDescription minPriorityDescription = minPriorityPair.first;
		int minPriority = minPriorityPair.second;
		
		Pair<AgentDescription, Integer> maxPriorityPair =
				helpmates.exportMaxPrioritizedDescription();
		AgentDescription maxPriorityDescription = maxPriorityPair.first;
		int maxPriority = maxPriorityPair.second;
		
		String minPriorityAgentName = minPriorityDescription.getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The worst: " + minPriorityAgentName + " priority: " + minPriority);

		String maxPriorityAgentName = maxPriorityDescription.getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The best : " + maxPriorityAgentName + " priority: " + maxPriority);
		
	}
	
	static InputRePlan killWorstAndReplaceByNewMethod(JobRun job,
			Iteration iteration, ModelOfHelpmates helpmates,
			InputAgentDescription newMethod) throws Exception {
		
		Pair<AgentDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		AgentDescription methodToKill = minPriorityPair.first;
		
		return new InputRePlan(iteration, methodToKill, newMethod);
	}
	
	static InputRePlan killWorstAndDuplicateBestHelpmate(
			JobRun job, Iteration iteration, ModelOfHelpmates helpmates) throws Exception {

		Pair<AgentDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		AgentDescription methodToKill = minPriorityPair.first;
		
		Pair<AgentDescription, Integer> maxPriorityPair =
				helpmates.exportMaxPrioritizedDescription();
		AgentDescription maxPriorityDesc = maxPriorityPair.first;
		InputAgentDescription methodToCreate =
				maxPriorityDesc.exportInputAgentDescription();
		
		return new InputRePlan(iteration, methodToKill, methodToCreate);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}
	
}
