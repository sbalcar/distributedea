package org.distributedea.agents.systemagents.centralmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.datamanager.DataManagerService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.Job;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.ProblemToolEvaluation;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

public class StartComputingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private String jobID;
	private Class<?> scheduler;
	private Class<?> problemToSolve;
	private String problemFileName;
	private String methodsFileName;
	private List<Class<?>> availableProblemTools;
	private AgentLogger logger;
	
	public StartComputingBehaviour(
			Job job,
			AgentLogger logger) {
		
		this.jobID = job.getJobID();
		this.scheduler = job.getScheduler();
		this.problemToSolve = job.getProblemToSolve();
		this.problemFileName = job.getProblemFileName();
		this.methodsFileName = job.getMethodsFileName();
		this.availableProblemTools = job.getAvailableProblemTools();
		this.logger = logger;
		
	}
	
	@Override
	public void action() {
		logger.log(Level.INFO, "Starts Computing behaviour");
		
		try {
			startCommand();
		} catch (SchedulerException e) {
			logger.logThrowable("Computing was stoped", e);
		}
		
	}

	
	protected void startCommand() throws SchedulerException {
		
		// tests validation of Problem Tools
		if (availableProblemTools.size() == 0) {
			logger.log(Level.INFO, "Incorrect input: Any problemTool available");
			throw new SchedulerException("Any problemTool available");
		}
		
		for (Class<?> problemToolClassI : availableProblemTools) {
			
			ProblemTool problemToolI = null;
			try {
				problemToolI = (ProblemTool) problemToolClassI.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				logger.logThrowable("Error with instance ProblemTool", e);
				throw new SchedulerException("Error with instance ProblemTool");
			}
			
			if (problemToolI.problemWhichSolves() != problemToSolve) {
				logger.log(Level.INFO, "ProblemTool " +
						problemToolI.getClass().getSimpleName() +
						"doesn't solve " + problemToSolve.getSimpleName() +
						" problem");
				throw new SchedulerException("Wrong ProblemTool");
			}
		}
		
		// AgentConfigurations - Methods reading
		XmlConfigurationProvider configProvider =
				new XmlConfigurationProvider();
		AgentConfigurations configuration =
				configProvider.getConfiguration(methodsFileName, logger);
		List<AgentConfiguration> agentConfigurations = configuration
				.getAgentConfigurations();

		if (agentConfigurations.isEmpty()) {
			logger.log(Level.INFO, "Any agent-method available");
			throw new SchedulerException("Any agent-method available");
		}
		
		// Problem reading and testing
		Class<?> problemToolClass1 = availableProblemTools.get(0);
		ProblemTool problemTool = null;
		try {
			problemTool = (ProblemTool) problemToolClass1.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.logThrowable("ProblemTool wasn't initialized", e);
			throw new SchedulerException("ProblemTool wasn't initialized");
		}
		
		Problem problem = problemTool.readProblem(problemFileName, logger);
		
		Scheduler scheduler;
		try {
			scheduler = (Scheduler) this.scheduler.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			logger.logThrowable("Scheduller wasn't initialized", e1);
			throw new SchedulerException("Scheduller wasn't initialized");
		}
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		scheduler.agentInitialization(centralManager, problem, jobID, agentConfigurations,
				availableProblemTools, logger);
		
		long generation = 0;
		while (scheduler.continueWithComputingInTheNextGeneration()) {
			
			// sleep
			try {
				Thread.sleep(Configuration.REPLAN_PERIOD_MS);
			} catch (InterruptedException e) {
				logger.logThrowable("Error by waiting for replan", e);
			}
			
			// Get Result of Computing
			AID [] aidOfComputingAgents = centralManager.searchDF(
					Agent_ComputingAgent.class.getName());
			
			List<ResultOfComputing> resultOfComputingAgents = new ArrayList<ResultOfComputing>();
			for (AID computingAgentI : aidOfComputingAgents) {
							
				ResultOfComputing resultOfComputingI =
						ComputingAgentService.sendAccessesResult(centralManager, computingAgentI, logger);
				resultOfComputingAgents.add(resultOfComputingI);
			}
			
			ResultOfComputing resultI = getBestResultOfComputing(resultOfComputingAgents, problem);
			saveResult(resultI);
			
			// log information about re-planning
			logger.log(Level.INFO, "Replanning: " + generation++);
			
			// re-planning
			scheduler.replan(centralManager, problem, agentConfigurations,
					availableProblemTools, logger);
		}
		scheduler.exit(centralManager, logger);

	}
	
	private ResultOfComputing getBestResultOfComputing(List<ResultOfComputing> resultOfComputingAgents, Problem problem) {
		
		if (resultOfComputingAgents == null || resultOfComputingAgents.isEmpty()) {
			return null;
		}
		
		ResultOfComputing bestResult = resultOfComputingAgents.get(0);
		
		for (ResultOfComputing resultOfComputingI : resultOfComputingAgents) {
			
			double fitnessValueI = resultOfComputingI.getFitnessValue();
			
			boolean isBetter = ProblemToolEvaluation.isFistFitnessBetterThanSecond(
					fitnessValueI, bestResult.getFitnessValue(), problem);
			if (isBetter) {
				bestResult = resultOfComputingI;
			}
		}
		
		return bestResult;
	}
	
	private void saveResult(ResultOfComputing bestResult) {
		
		if (bestResult != null) {
			logger.log(Level.INFO, "" + bestResult.getFitnessValue());
		
			DataManagerService.sendResultOfComputing((Agent_DistributedEA) this.myAgent, bestResult, logger);
		}
	}
	
}
