package org.distributedea.agents.systemagents.centralmanager.scheduler;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.noontology.Job;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;
import org.distributedea.problems.ProblemToolEvaluation;

public class SchedulerFollowBestResult implements Scheduler {
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			Job job, AgentConfigurations configurations,
			AgentLogger logger) throws SchedulerException {
		
		SchedulerInitialization schedullerInit = new SchedulerInitialization();
		schedullerInit.agentInitialization(centralManager, job,
				configurations, logger);
	}

	/**
	 * Nothing is changing
	 * @param centralManager
	 * @param problemTool
	 * @param logger
	 */
	@Override
	public void replan(Agent_CentralManager centralManager,
			Job job, AgentConfigurations configurations,
			AgentLogger logger)  throws SchedulerException {		
		
		AID [] aidOfComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		List<ResultOfComputing> resultOfComputingAgents = new ArrayList<ResultOfComputing>();
		for (AID computingAgentI : aidOfComputingAgents) {
						
			ResultOfComputing resultOfComputingI =
					ComputingAgentService.sendAccessesResult(centralManager, computingAgentI, logger);
			resultOfComputingAgents.add(resultOfComputingI);
		}
		
		if (resultOfComputingAgents.isEmpty()) {
			SchedulerException exception= new SchedulerException("No results available");;
			logger.logThrowable("No results available", exception);
			throw exception;
		}
		
		ResultOfComputing bestResult = resultOfComputingAgents.get(0);
		ResultOfComputing worstResult = resultOfComputingAgents.get(0);
		
		for (ResultOfComputing resultI : resultOfComputingAgents) {
			
			double bestFitnessI = bestResult.getFitnessValue();
			double worstFitnessI = worstResult.getFitnessValue();
			double fitnessNew = resultI.getFitnessValue();
			
			boolean isNewIndividualBetter =
					ProblemToolEvaluation.isFistFitnessBetterThanSecond(
							fitnessNew, bestFitnessI, job.getProblem());
			boolean isNewIndividualWorse =
					ProblemToolEvaluation.isFistFitnessWorseThanSecond(
							fitnessNew, worstFitnessI, job.getProblem());
			if (isNewIndividualBetter) {
				bestResult = resultI;
			}
			if (isNewIndividualWorse) {
				worstResult = resultI;
			}
		}
		
		int worstIndex = resultOfComputingAgents.indexOf(worstResult);
		AID worstAID = aidOfComputingAgents[worstIndex];

		AgentConfiguration bestConfiguration = bestResult.exportAgentConfiguration();
		AgentDescription bestDescription = bestResult.getAgentDescription();
		Class<?> bestProblemToolClass = bestDescription.exportProblemToolClass();

		ProblemStruct problemStruct = new ProblemStruct();
		problemStruct.setJobID(job.getJobID());
		problemStruct.setIndividualDistribution(job.getIndividualDistribution());
		problemStruct.setProblem(job.getProblem());
		problemStruct.setProblemToolClass(bestProblemToolClass.getName());
		
		SchedulerTool.killAndCreateAgent(centralManager, worstAID,
				bestConfiguration, problemStruct, logger);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
