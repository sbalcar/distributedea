package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.logging.Level;

import jade.core.AID;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitialization;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.Iteration;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;


public class SchedulerFollowBestResult implements Scheduler {
	
	private SchedulerInitialization schedullerInit = null; 
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager, JobRun job,
			AgentLogger logger) throws SchedulerException {
		
		logger.log(Level.INFO, "Scheduler " + getClass().getSimpleName() + " initialization");
		
		SchedulerInitializationState state = SchedulerInitializationState.RUN_ONE_AGENT_PER_CORE;
		
		schedullerInit = new SchedulerInitialization(state, true);
		schedullerInit.agentInitialization(centralManager, job, logger);
	}

	/**
	 * Nothing is changing
	 * @param centralManager
	 * @param problemTool
	 * @param logger
	 */
	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			 Iteration iteration, ReceivedData receivedData, AgentLogger logger
			 ) throws SchedulerException {
		
		// initialization of Methods on the new containers
		schedullerInit.replan(centralManager, job, iteration, receivedData, logger);
		
		Problem problem = job.getProblem();
		
		ResultOfComputingWrapper resultsOfComputing = 
				SchedulerTool.getResultOfComputings(centralManager, logger);
		
		ResultOfComputing bestResult = resultsOfComputing.exportBestResultOfComputing(problem);
		ResultOfComputing worstResult = resultsOfComputing.exportWorstResultOfComputing(problem);
		
		if (bestResult == null || worstResult == null) {
			return;
		}
		
		String worstResultAgentName = worstResult.getAgentDescription().getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The worst: " + worstResultAgentName + " fitness: " + worstResult.getFitnessValue());

		String bestResultAgentName = bestResult.getAgentDescription().getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The best : " + bestResultAgentName + " fitness: " + bestResult.getFitnessValue());
		
		
		AID worstAID = worstResult.exportAgentConfiguration().exportAgentAID();
		
		AgentConfiguration bestConfiguration = bestResult.exportAgentConfiguration();
		AgentDescription bestDescription = bestResult.getAgentDescription();
		Class<?> bestProblemToolClass = bestDescription.exportProblemToolClass();

		
		Pair<AgentConfiguration, Class<?>> newMethod =
				cooseNewMethod(bestConfiguration, bestProblemToolClass);
		AgentConfiguration newAgentConfiguration = newMethod.first;
		Class<?> newProblemToolClass = newMethod.second;
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
				
		SchedulerTool.killAndCreateAgent(centralManager, worstAID,
				newAgentConfiguration, problemStruct, logger);
	}
	
	private Pair<AgentConfiguration, Class<?>> cooseNewMethod(
			AgentConfiguration bestConfiguration, Class<?> bestProblemToolClass) {
		
		AgentDescription candidateDescription = schedullerInit.removeNextCandidate();
		
		if (candidateDescription != null) {
			
			return new Pair<AgentConfiguration, Class<?>>(
					candidateDescription.getAgentConfiguration(),
					candidateDescription.exportProblemToolClass());
		}
		
		return new Pair<AgentConfiguration, Class<?>>(
				bestConfiguration, bestProblemToolClass);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
