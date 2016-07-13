package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methodinstancedescription.MethodsResults;
import org.distributedea.ontology.monitor.MethodStatistic;

public class PlannerFollowTheGreatestQuantityOfImprovement implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws PlannerException {

		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		
		plannerInit = new PlannerInitialisation(state, true);
		return plannerInit.agentInitialisation(centralManager, iteration, jobRun, logger);
	}

	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history)
			throws PlannerException {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		RePlan rePlan = replanning(iteration, history);
		PlannerTool.processReplanning(centralManager, rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlan);
	}
	
	private RePlan replanning(Iteration iteration, History history)
			throws PlannerException {

		History historyOfCurrentMethods = history.
				exportHistoryOfRunningMethods(iteration, 3);
				
		if (historyOfCurrentMethods.getMethods().isEmpty()) {
			return new RePlan(iteration);
		}
		
		MethodsResults statistic = history.exportMethodsResults(iteration);
		MethodStatistic greatestQuantMethodStatistic = statistic.
				exportMethodAchievedTheGreatestQuantityOfImprovement();
		MethodStatistic leastQuantMethodStatistic = statistic.
				exportMethodAchievedTheLeastQuantityOfImprovement();
		
		AgentDescription methodToKill =
				leastQuantMethodStatistic.getAgentDescription();
		AgentDescription methodGreatestQuant =
				greatestQuantMethodStatistic.getAgentDescription();
		
		AgentDescription candidateMethod = plannerInit.removeNextCandidate();
		if (candidateMethod != null) {

			return new RePlan(iteration, methodToKill,
					candidateMethod);
		}
		
		return new RePlan(iteration, methodToKill,
				methodGreatestQuant);
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
	}

}
