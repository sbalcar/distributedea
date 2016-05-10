package org.distributedea.agents.systemagents.centralmanager.behaviours;


import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.datamanager.DataManagerService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.job.noontology.Job;
import org.distributedea.ontology.job.noontology.JobWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;
import org.distributedea.problems.ProblemTool;

import jade.core.behaviours.OneShotBehaviour;

public class StartComputingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private String jobID;
	private long countOfReplaning;
	private Scheduler scheduler;
	private Class<?> problemToSolve;
	private String problemFileName;
	private String methodsFileName;
	private ProblemTools problemTools;
	private AgentLogger logger;
	
	public StartComputingBehaviour(
			JobWrapper job, AgentLogger logger) {
		
		this.jobID = job.getJobID();
		this.countOfReplaning = job.getCountOfReplaning();
		this.scheduler = job.getScheduler();
		this.problemToSolve = job.getProblemToSolve();
		this.problemFileName = job.getProblemFileName();
		this.methodsFileName = job.getMethodsFileName();
		this.problemTools = job.getProblemTools();
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
		
		boolean areProblemToolsValid = problemTools.valid(problemToSolve, logger);
		if (! areProblemToolsValid) {
			throw new SchedulerException("ProblemTools aren't valid");
		}
		
		// AgentConfigurations - Methods reading
		AgentConfigurations agentConfigurations =
				XmlConfigurationProvider.getConfiguration(methodsFileName, logger);
		if (agentConfigurations == null) {
			throw new SchedulerException("Can not read AgetConfigurations");
		}
		
		boolean areAgentConfigurationsValid = agentConfigurations.valid(logger);
		if (! areAgentConfigurationsValid) {
			throw new SchedulerException("AgentConfiguration aren't valid");
		}
	
		
		// Problem reading and testing
		ProblemTool problemTool = problemTools.exportProblemTool(0, logger);
		Problem problem = problemTool.readProblem(problemFileName, logger);
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		
		Job job = new Job();
		job.setJobID(jobID);
		job.setIndividualDistribution(areAgentConfigurationsValid);
		job.setProblem(problem);
		job.setProblemTools(problemTools);
		
		
		scheduler.agentInitialization(centralManager, job,
				agentConfigurations, logger);
		
		long generation = 0;
		while (generation < countOfReplaning) {
			
			// sleep
			try {
				Thread.sleep(Configuration.REPLAN_PERIOD_MS);
			} catch (InterruptedException e) {
				logger.logThrowable("Error by waiting for replan", e);
			}
			
			ResultOfComputingWrapper resultOfComputingAgents =
					ComputingAgentService.sendAccessesResult(centralManager, logger);
			
			ResultOfComputing resultI = resultOfComputingAgents.exportBestResultOfComputing(problem);
			saveResult(resultI);
			
			// log information about re-planning
			logger.log(Level.INFO, "Replanning: " + generation++ + " / " + countOfReplaning);
			
			// re-planning
			scheduler.replan(centralManager, job, agentConfigurations,
					logger);
		}
		scheduler.exit(centralManager, logger);

	}
	

	
	private void saveResult(ResultOfComputing bestResult) {
		
		if (bestResult != null) {
			logger.log(Level.INFO, "" + bestResult.getFitnessValue());
		
			DataManagerService.sendResultOfComputing((Agent_DistributedEA) this.myAgent, bestResult, logger);
		}
	}
	
}
