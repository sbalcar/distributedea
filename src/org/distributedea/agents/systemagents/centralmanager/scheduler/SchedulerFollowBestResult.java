package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.logging.Level;

import jade.core.AID;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.Job;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;


public class SchedulerFollowBestResult implements Scheduler {
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager, Job job,
			AgentConfigurations configurations, AgentLogger logger) throws SchedulerException {
		
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
	public void replan(Agent_CentralManager centralManager, Job job,
			AgentConfigurations configurations, AgentLogger logger) throws SchedulerException {		
		
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

		ProblemStruct problemStruct = job.exportProblemStruct(bestProblemToolClass);
				
		SchedulerTool.killAndCreateAgent(centralManager, worstAID,
				bestConfiguration, problemStruct, logger);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
