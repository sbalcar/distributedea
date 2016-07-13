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
import org.distributedea.ontology.helpmate.HelpmatesWrapper;
import org.distributedea.ontology.job.JobRun;

public class PlannerFollowup3Helpers implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	private int MIN_NUMER_OF_METHODS = 3;
	
	public PlannerFollowup3Helpers() {} // for serialization
	
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
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws PlannerException {
		
		// initialization of Methods on the new containers
		Pair<Plan,RePlan> rePlanNew = plannerInit.replan(iteration, history);
		
		// process RePlan
		RePlan rePlan = replanning(iteration, history);
		PlannerTool.processReplanning(centralManager, rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanNew.first, rePlan);
	}
	
	private RePlan replanning(Iteration iteration, History history
				 ) throws PlannerException {
		HelpmatesWrapper helpmates = PlannerTool.getHelpmates(
				centralManager, NEW_STATISTICS_FOR_EACH_QUERY, logger);
		
		PlannerFollowupHelpers.printLog(centralManager, helpmates, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return new RePlan(iteration);
		}
		
		AgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			return PlannerFollowupHelpers.killWorstAndReplaceByNewMethod(
					jobRun, iteration, helpmates, candidateDescription);
		}
		
		//obtain the number of active helpmates
		int numberOfDifferentAD = helpmates.
				exportNumberOfDifferentAgentDescription();
		
		//if number of helpmates is min defined
		if (numberOfDifferentAD <= MIN_NUMER_OF_METHODS) {
			
			//obtain the number of duplicates of minimal prioritized method
			Pair<AgentDescription, Integer> minPriorityPair =
					helpmates.exportMinPrioritizedDescription();
			AgentDescription minPriorityDescription = minPriorityPair.first;
			
			int numberOf = helpmates.exportNumberConcreteAgentDescription(
					minPriorityDescription);
			//if the method is in the system only once
			if (numberOf == 1) {
				return new RePlan(iteration);
			}
		}
		
		//rozsireni: pokud budu takto vybijet vicekrat tu samou stratgii -> prohod ji za nejakou ktera v systemu neni
		
		return PlannerFollowupHelpers.killWorstAndDuplicateBestHelpmate(
				jobRun, iteration, helpmates);

	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
	}

}
