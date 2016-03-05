package org.distributedea.agents.systemagents.centralmanager;

import java.util.List;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerSimple;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;

import jade.core.behaviours.OneShotBehaviour;

public class StartComputingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Class<?> problemToSolve;
	private String problemFileName;
	private String methodsFileName;
	private Class<?> [] availableProblemTools;
	private AgentLogger logger;
	
	public StartComputingBehaviour(
			Class<?> problemToSolveClassName,
			String problemFileName,
			String methodsFileName,
			Class<?> [] availableProblemToolsClassName,
			AgentLogger logger) {
		
		this.problemToSolve = problemToSolveClassName;
		this.problemFileName = problemFileName;
		this.methodsFileName = methodsFileName;
		this.availableProblemTools = availableProblemToolsClassName;
		this.logger = logger;
		
	}
	
	@Override
	public void action() {
		startCommand();
		
	}

	
	protected void startCommand() {
		
		if (problemToSolve == null) {
			throw new IllegalStateException("Parameter problemToSolve is null");
		}
		if (problemFileName == null) {
			throw new IllegalStateException("Parameter problemFileName is null");
		}
		if (methodsFileName == null) {
			throw new IllegalStateException("Parameter methodsFileName is null");
		}
		if (availableProblemTools == null) {
			throw new IllegalStateException("Parameter availableProblemTools is null");
		}
		if (logger == null) {
			throw new IllegalStateException("Parameter logger is null");
		}		
		
		// tests validation of Problem Tools
		if (availableProblemTools.length == 0) {
			logger.log(Level.INFO, "Incorrect input: Any problemTool available");
			return;
		}
		for (Class<?> problemToolClassI : availableProblemTools) {
			try {
				ProblemTool problemToolI = (ProblemTool) problemToolClassI.newInstance();
				
				if (problemToolI.problemWhichSolves() != problemToSolve) {
					logger.log(Level.INFO, "Incorrect input: ProblemTool " +
							problemToolI.getClass().getSimpleName() +
							"doesn't solve " + problemToSolve.getSimpleName() +
							" problem");
					return;
				}
				
			} catch (InstantiationException e) {
				logger.logThrowable("Error with instance ProblemTool", e);
			} catch (IllegalAccessException e) {
				logger.logThrowable("Error with instance ProblemTool", e);
			}
		}
		
		// AgentConfigurations - Methods reading
		XmlConfigurationProvider configProvider =
				new XmlConfigurationProvider();
		AgentConfigurations configuration =
				configProvider.getConfiguration(methodsFileName, logger);
		List<AgentConfiguration> agentConfigurations = configuration
				.getAgentConfigurations();
		
		AgentConfiguration[] configurations = 
				agentConfigurations.toArray(
				new AgentConfiguration[agentConfigurations.size()]);
		if (configurations.length == 0) {
			logger.log(Level.INFO, "Incorrect input: Any agent-method available");
		}
		
		// Problem reading and testing
		Class<?> problemToolClass1 = availableProblemTools[0];
		ProblemTool problemTool = null;
		try {
			problemTool = (ProblemTool) problemToolClass1.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.logThrowable("ProblemTool wasn't initialized", e);
			throw new IllegalStateException("ProblemTool wasn't initialized");
		}
		
		Problem problem = problemTool.readProblem(problemFileName, logger);
		if (problem == null) {
			logger.log(Level.INFO, "Problem wasn't loaded");
		}
		
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		
		
		
		Scheduler scheduler;
		try {
			scheduler = (Scheduler) InputConfiguration.scheduler.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			logger.logThrowable("Scheduller wasn't initialized", e1);
			throw new IllegalStateException("Scheduller wasn't initialized");
		}
		
		scheduler.agentInitialization(centralManager, problem, configurations,
				availableProblemTools, logger);
		
		
//*		
		while (true) {
			try {
				Thread.sleep(Configuration.REPLAN_PERIOD_MS);
			} catch (InterruptedException e) {
				logger.logThrowable("Error by waiting for replan", e);
			}
						
			logger.log(Level.INFO, "Replanning");
			scheduler.replan(centralManager, problem, configurations,
					availableProblemTools, logger);

		}
//*/
	}
	
}
