package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitialization;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.helpmate.HelpmatesWrapper;
import org.distributedea.ontology.job.JobRun;

public class PlannerFollowup3Helpers implements Planner {

	private PlannerInitialization plannerInit = null;
	
	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	private int MIN_NUMER_OF_METHODS = 3;
	
	public PlannerFollowup3Helpers() {} // for serialization
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws PlannerException {

		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		PlannerInitializationState state = PlannerInitializationState.RUN_ONE_AGENT_PER_CORE;
		
		plannerInit = new PlannerInitialization(state, true);
		plannerInit.agentInitialization(centralManager, job, logger);
		
	}

	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			 Iteration iteration, ReceivedData receivedData, AgentLogger logger
			 ) throws PlannerException {
		
		// initialization of Methods on the new containers
		plannerInit.replan(centralManager, job, iteration, receivedData, logger);
		
		HelpmatesWrapper helpmates = PlannerTool.getHelpmates(
				centralManager, NEW_STATISTICS_FOR_EACH_QUERY, logger);
		
		PlannerFollowupHelpers.printLog(centralManager, helpmates, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return;
		}
		
		AgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			PlannerFollowupHelpers.killWorstAndReplaceByNewMethod(
					centralManager, job, helpmates, candidateDescription, logger);
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
				return;
			}
		}
				
		//rozsireni: pokud budu takto vybijet vicekrat tu samou stratgii -> prohod ji za nejakou ktera v systemu neni
		
		PlannerFollowupHelpers.killWorstAndDuplicateBestHelpmate(
				centralManager, job, helpmates, logger);

	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
	}

}
