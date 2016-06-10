package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.logging.Level;

import jade.core.AID;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitialization;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;


public class PlannerFollowBestResult implements Planner {
	
	private PlannerInitialization plannerInit = null; 
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager, JobRun job,
			AgentLogger logger) throws PlannerException {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		PlannerInitializationState state = PlannerInitializationState.RUN_ONE_AGENT_PER_CORE;
		
		plannerInit = new PlannerInitialization(state, true);
		plannerInit.agentInitialization(centralManager, job, logger);
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
			 ) throws PlannerException {
		
		// initialization of Methods on the new containers
		plannerInit.replan(centralManager, job, iteration, receivedData, logger);
		
		ResultOfComputingWrapper resultOfComputingWrapper =
				receivedData.getResultOfComputingWrapper();
				
		printLog(centralManager, job, resultOfComputingWrapper, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return;
		}
		
		if (! resultOfComputingWrapper.exportContainsMoreThanOneMethod()) {
			return;
		}
		
		AgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			killWorstAndReplaceByNewMethod(centralManager, job,
					resultOfComputingWrapper, candidateDescription, logger);
		}
		
		killWorstAndDuplicateBestMethod(centralManager, job,
				resultOfComputingWrapper, logger);
	}
	
	private void printLog(
			Agent_CentralManager centralManager, JobRun job,
			ResultOfComputingWrapper resultOfComputingWrapper,
			AgentLogger logger) throws PlannerException {
		
		Problem problem = job.getProblem();
		
		ResultOfComputing bestResult = resultOfComputingWrapper
				.exportBestResultOfComputing(problem);
		ResultOfComputing worstResult = resultOfComputingWrapper
				.exportWorstResultOfComputing(problem);
		
		String worstResultAgentName = worstResult.getAgentDescription().getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The worst: " + worstResultAgentName + " fitness: " + worstResult.getFitnessValue());

		String bestResultAgentName = bestResult.getAgentDescription().getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The best : " + bestResultAgentName + " fitness: " + bestResult.getFitnessValue());
	}
	
	static void killWorstAndReplaceByNewMethod(
			Agent_CentralManager centralManager, JobRun job,
			ResultOfComputingWrapper resultOfComputingWrapper,
			AgentDescription newMethod, AgentLogger logger) throws PlannerException {

		Problem problem = job.getProblem();
		
		ResultOfComputing worstResult = resultOfComputingWrapper
				.exportWorstResultOfComputing(problem);
		
		AID worstAID = worstResult.exportAgentConfiguration().exportAgentAID();
		

		
		AgentConfiguration newAgentConfiguration = newMethod.getAgentConfiguration();
		Class<?> newProblemToolClass = newMethod.exportProblemToolClass();
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
				
		PlannerTool.killAndCreateAgent(centralManager, worstAID,
				newAgentConfiguration, problemStruct, logger);
	}
	
	private void killWorstAndDuplicateBestMethod(
			Agent_CentralManager centralManager, JobRun job,
			ResultOfComputingWrapper resultOfComputingWrapper,
			AgentLogger logger) throws PlannerException {

		Problem problem = job.getProblem();
		
		ResultOfComputing bestResult = resultOfComputingWrapper
				.exportBestResultOfComputing(problem);
		ResultOfComputing worstResult = resultOfComputingWrapper
				.exportWorstResultOfComputing(problem);
		
		AID worstAID = worstResult.exportAgentConfiguration().exportAgentAID();
		
		AgentDescription bestDescription = bestResult.getAgentDescription();
		AgentConfiguration bestConfiguration = bestDescription.getAgentConfiguration();
		Class<?> bestProblemToolClass = bestDescription.exportProblemToolClass();
		
		ProblemStruct problemStruct = job.exportProblemStruct(bestProblemToolClass);
				
		PlannerTool.killAndCreateAgent(centralManager, worstAID,
				bestConfiguration, problemStruct, logger);
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
