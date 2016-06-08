package org.distributedea.agents.systemagents.centralmanager.scheduler;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import jade.core.AID;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitialization;
import org.distributedea.agents.systemagents.centralmanager.scheduler.initialization.SchedulerInitializationState;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.Iteration;
import org.distributedea.agents.systemagents.centralmanager.scheduler.models.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerTool;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescriptionwrapper.MethodDescriptionsWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class SchedulerMethodDescription implements Scheduler {

	private SchedulerInitialization schedullerInit = null;
	
	private MethodDescriptionsWrapper knownMethods;
	
	private Random ran = new Random();
	
	
	public SchedulerMethodDescription() {} // for serialization
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws SchedulerException {
		
		logger.log(Level.INFO, "Scheduler " + getClass().getSimpleName() + " initialization");

		// get all available Agent Description
		SchedulerInitializationState state = SchedulerInitializationState.RUN_ALL_COMBINATIONS;
		
		SchedulerInitialization scheduller = new SchedulerInitialization(state, true);
		scheduller.agentInitializationOnlyCreateAgents(centralManager, job, logger);
		
		knownMethods = ComputingAgentService.sendGetMethodDescriptions(centralManager, logger);
		scheduller.exit(centralManager, logger);
		
		
		// initialize one agent per core
		SchedulerInitializationState stateInit = SchedulerInitializationState.RUN_ONE_AGENT_PER_CORE;
		
		MethodDescriptionsWrapper exploitationMethodDescriptionsWrapper =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<AgentConfiguration> exploitationAgentConfigurations =
				exploitationMethodDescriptionsWrapper.exportAgentConfigurations();
		AgentConfigurations agentConfigurations =
				new AgentConfigurations(exploitationAgentConfigurations);
		
		JobRun exploitationJobRun = new JobRun(job);
		exploitationJobRun.setAgentConfigurations(agentConfigurations);
		
		schedullerInit = new SchedulerInitialization(stateInit, true);
		schedullerInit.agentInitialization(centralManager, exploitationJobRun, logger);
	}

	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			 Iteration iteration, ReceivedData receivedData, AgentLogger logger
			 ) throws SchedulerException {
		
		schedullerInit.replan(centralManager, job, iteration, receivedData, logger);

		// get ratio of exploration and exploatation
		ResultOfComputingWrapper resultOfComputingWrapper =
				receivedData.getResultOfComputingWrapper();
		
		AgentDescription candidate = schedullerInit.removeNextCandidate();
		if (candidate != null) {
			// remove worst and replace by new candidate
			logger.log(Level.INFO, "Trying next candidate");
			replaceWorstByCandidate(centralManager, job,
					resultOfComputingWrapper, candidate, logger);
			return;
		}
		
		MethodDescriptionsWrapper exploitationMethodDescriptions =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<Class<?>> exploitationAgentTypes = 
				exploitationMethodDescriptions.exportAgentTypes();
		
		int numOfAllAgent = resultOfComputingWrapper.getResultOfComputing().size();
		int numOfExploitationAgentTypes = resultOfComputingWrapper
				.exportAgentNumberOfType(exploitationAgentTypes);
		
		int numOfExplorationAgentTypes = numOfAllAgent - numOfExploitationAgentTypes;
		
		double ratioExplorationDivSize = (double)numOfExplorationAgentTypes / (double) numOfAllAgent;
		logger.log(Level.INFO, "Exploitation / Exploration = " +
				numOfExploitationAgentTypes + " / " + numOfExplorationAgentTypes);
		
		double ratioIteration = iteration.exportRationOfIteration();

		if (ratioExplorationDivSize + 0.1 < ratioIteration) {

			logger.log(Level.INFO, "Less Exploitation, more Exploration methods");
			replaceWorstExploitationMethods(centralManager, job,
					resultOfComputingWrapper, logger);
		}
	}
	
	private void replaceWorstByCandidate(Agent_CentralManager centralManager, JobRun job,
			ResultOfComputingWrapper resultOfComputingWrapper, AgentDescription candidate,
			AgentLogger logger) throws SchedulerException {
		
		Problem problem = job.getProblem();
		
		ResultOfComputing worstResultOfComputing =
				resultOfComputingWrapper.exportWorstResultOfComputing(problem);
		AgentConfiguration worstAgentConfiguration =
				worstResultOfComputing.getAgentDescription().getAgentConfiguration();
		AID aidToKill = worstAgentConfiguration.exportAgentAID();
		
		AgentConfiguration newAgentConfiguration = candidate.getAgentConfiguration();
		Class<?> newProblemToolClass = candidate.exportProblemToolClass();
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
		
		SchedulerTool.killAndCreateAgent(centralManager, aidToKill,
				newAgentConfiguration, problemStruct, logger);
	}
	
	public void replaceWorstExploitationMethods(Agent_CentralManager centralManager,
			JobRun job, ResultOfComputingWrapper resultOfComputingWrapper,
			AgentLogger logger) throws SchedulerException {
			
		Problem problem = job.getProblem();
		List<Class<?>> problemTools = job.getProblemTools().getProblemTools();
		
		
		
		MethodDescriptionsWrapper availableExploitationMethods =
				knownMethods.exportExploitationMethodDescriptionsWrapper();
		List<Class<?>> availableExploitationAgentTypes =
				availableExploitationMethods.exportAgentTypes();
		// choosing agent exploitation Configuration to kill		
		ResultOfComputingWrapper exploitationResults =
				resultOfComputingWrapper.exportResultsOfGivenAgentType(availableExploitationAgentTypes);
		ResultOfComputing worst =
				exploitationResults.exportWorstResultOfComputing(problem);
		//if system don't contain any exploitation method
		if (worst == null) {
			worst = resultOfComputingWrapper.exportWorstResultOfComputing(problem);
		}
		
		AID aidToKill = worst.exportAgentConfiguration().exportAgentAID();
		
		

		// choose method description to create new agent
		MethodDescriptionsWrapper availableExplorationMethods =
				knownMethods.exportExplorationMethodDescriptions();
		List<Class<?>> availableExplorationAgentTypes =
				availableExplorationMethods.exportAgentTypes();
		
		List<Class<?>> notRunningExplorationAgentTypes = resultOfComputingWrapper.
				exportAgentTypesWhichDontContains(availableExplorationAgentTypes);
		
		AgentConfiguration newAgentConfiguration;
		Class<?> newProblemToolClass;
		
		// if exists some exploration type of agent which is not running -> start random of that 
		if (! notRunningExplorationAgentTypes.isEmpty()) {
			
			int agentTypeIndex = ran.nextInt(notRunningExplorationAgentTypes.size());
			int problemToolIndex = ran.nextInt(problemTools.size());
			
			Class<?> selectedAgentType = notRunningExplorationAgentTypes.get(agentTypeIndex);
			Class<?> selectedProblemTool = problemTools.get(problemToolIndex);
			
			newAgentConfiguration = new AgentConfiguration(selectedAgentType, null);
			newProblemToolClass = selectedProblemTool;
			
		} else {
			
			// duplicate the best exploration agent
			ResultOfComputingWrapper explorationResultOfComputingWrp =
					resultOfComputingWrapper.exportResultsOfGivenAgentType(availableExplorationAgentTypes);
			ResultOfComputing bestExplorationResultOfComputingWrp =
					explorationResultOfComputingWrp.exportBestResultOfComputing(problem);
			
			AgentDescription agentDescription =
					bestExplorationResultOfComputingWrp.getAgentDescription();
			
			newAgentConfiguration = agentDescription.getAgentConfiguration();
			newProblemToolClass = agentDescription.exportProblemToolClass();
		}
		
		
		ProblemStruct problemStruct = job.exportProblemStruct(newProblemToolClass);
		
		SchedulerTool.killAndCreateAgent(centralManager, aidToKill,
				newAgentConfiguration, problemStruct, logger);
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		SchedulerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
