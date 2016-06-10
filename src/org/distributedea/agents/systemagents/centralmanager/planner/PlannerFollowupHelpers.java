package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.logging.Level;

import jade.core.AID;

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
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.HelpmatesWrapper;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class PlannerFollowupHelpers implements Planner {

	private PlannerInitialization plannerInit = null;
	
	private boolean NEW_STATISTICS_FOR_EACH_QUERY = true;
	
	public PlannerFollowupHelpers() {} // for serialization
	
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
		
		// pring info
		printLog(centralManager, helpmates,logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return;
		}
		
		AgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			killWorstAndReplaceByNewMethod(centralManager, job,
					helpmates, candidateDescription, logger);
		}
		
		killWorstAndDuplicateBestHelpmate(centralManager, job, helpmates, logger);

	}
	
	static void printLog(Agent_CentralManager centralManager,
			HelpmatesWrapper helpmates, AgentLogger logger) throws PlannerException {
		
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
	
	static void killWorstAndReplaceByNewMethod(
			Agent_CentralManager centralManager, JobRun job,
			HelpmatesWrapper helpmates,
			AgentDescription newMethod, AgentLogger logger) throws PlannerException {
		
		Pair<AgentDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		AgentDescription minPriorityDescription = minPriorityPair.first;
		
		AID worstAID = minPriorityDescription.getAgentConfiguration().exportAgentAID();
		
		AgentConfiguration newAgentConfiguration = newMethod.getAgentConfiguration();
		Class<?> newProblemToolClass = newMethod.exportProblemToolClass();
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
		
		PlannerTool.killAndCreateAgent(centralManager, worstAID,
				newAgentConfiguration, problemStruct, logger);
	}
	
	static void killWorstAndDuplicateBestHelpmate(Agent_CentralManager centralManager,
			JobRun job, HelpmatesWrapper helpmates, AgentLogger logger) throws PlannerException {

		Pair<AgentDescription, Integer> minPriorityPair =
				helpmates.exportMinPrioritizedDescription();
		AgentDescription minPriorityDescription = minPriorityPair.first;
		
		Pair<AgentDescription, Integer> maxPriorityPair =
				helpmates.exportMaxPrioritizedDescription();
		AgentDescription maxPriorityDescription = maxPriorityPair.first;
		
		// agent configurations
		AgentConfiguration worstConfiguration =
				minPriorityDescription.getAgentConfiguration();

		// aid of the worst agent (agent to kill)
		AID worstAID = worstConfiguration.exportAgentAID();
		
		
		
		AgentConfiguration newAgentConfiguration =
				maxPriorityDescription.getAgentConfiguration();
		Class<?> newProblemToolClass =
				maxPriorityDescription.exportProblemToolClass();
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
		
		// kill agent with the smallest priority and run the agent with the highest priority
		PlannerTool.killAndCreateAgent(centralManager, worstAID,
				newAgentConfiguration, problemStruct, logger);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
	}
	
}
