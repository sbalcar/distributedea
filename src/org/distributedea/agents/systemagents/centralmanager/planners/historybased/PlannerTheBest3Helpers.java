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

public class PlannerTheBest3Helpers implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	private int MIN_NUMER_OF_METHODS = 3;
	
	public PlannerTheBest3Helpers() {} // for serialization
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws Exception {
		
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
			 ) throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan,RePlan> rePlanNew = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager, rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanNew.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
				 ) throws Exception {
		ModelOfHelpmates helpmates = ComputingAgentService.getHelpmates(
				centralManager, NEW_STATISTICS_FOR_EACH_QUERY, logger);
		
		PlannerTheBestHelper.printLog(centralManager, helpmates, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return new InputRePlan(iteration);
		}
		
		InputAgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			return PlannerTheBestHelper.killWorstAndReplaceByNewMethod(
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
				return new InputRePlan(iteration);
			}
		}
		
		//rozsireni: pokud budu takto vybijet vicekrat tu samou stratgii -> prohod ji za nejakou ktera v systemu neni
		
		return PlannerTheBestHelper.killWorstAndDuplicateBestHelpmate(
				jobRun, iteration, helpmates);

	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
