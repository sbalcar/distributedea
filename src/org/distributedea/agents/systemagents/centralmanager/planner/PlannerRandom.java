package org.distributedea.agents.systemagents.centralmanager.planner;

import jade.core.AID;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitialization;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class PlannerRandom implements Planner {
	
	private PlannerInitialization plannerInit = null;
	
	Random ran = new Random();
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager, JobRun job,
			AgentLogger logger) throws PlannerException {
		
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
		
		ResultOfComputingWrapper resultOfComputingWrapper =
				receivedData.getResultOfComputingWrapper();
		
		AgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			PlannerFollowBestResult.killWorstAndReplaceByNewMethod(
					centralManager, job, resultOfComputingWrapper, candidateDescription, logger);
		}
	
		replanRandomByRandomMethod(centralManager, job, iteration, receivedData, logger);
	}
	
	
	void replanRandomByRandomMethod(Agent_CentralManager centralManager, JobRun job,
				 Iteration iteration, ReceivedData receivedData, AgentLogger logger
				 ) throws PlannerException {			

		//random select agent to kill
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());
		
		int indexAID = ran.nextInt(aidComputingAgents.length);
		AID agentToKillAID = aidComputingAgents[indexAID];
		
		
		//random select configuration for creating the new agent
		AgentConfigurations configurations = job.getAgentConfigurations();
				
		//random select problem tool for the new computation
		List<Class<?>> problemTools = job.getProblemTools().getProblemTools();

		
		Pair<AgentConfiguration, Class<?>> newMethod =
				cooseRandomMethod(configurations, problemTools);
		AgentConfiguration agentToCreate = newMethod.first;
		Class<?> problemToolClass = newMethod.second;
				
		ProblemStruct problemStruct = job.exportProblemStruct(problemToolClass);
				
		PlannerTool.killAndCreateAgent(centralManager, agentToKillAID,
				agentToCreate, problemStruct, logger);
	}
	
	private Pair<AgentConfiguration, Class<?>> cooseRandomMethod(
			AgentConfigurations configurations, List<Class<?>> problemTools) {
	
		int indexAC = ran.nextInt(configurations.getAgentConfigurations().size());
		AgentConfiguration agentToCreate = configurations.exportAgentConfigurations(indexAC);
		
		int indexPT = ran.nextInt(problemTools.size());
		Class<?> problemToolClass = problemTools.get(indexPT);
		
		return new Pair<AgentConfiguration, Class<?>>(
				agentToCreate, problemToolClass);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}
}
