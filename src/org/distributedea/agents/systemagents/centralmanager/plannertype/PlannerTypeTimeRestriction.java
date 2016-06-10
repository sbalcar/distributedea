package org.distributedea.agents.systemagents.centralmanager.plannertype;

import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.datamanager.DataManagerService;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problem.Problem;

public class PlannerTypeTimeRestriction implements PlannerType {
	
	private Agent_CentralManager centralManager;
	private long countOfReplaning;
	private AgentLogger logger;
	
	public PlannerTypeTimeRestriction(Agent_CentralManager agent,
			long countOfReplaning, AgentLogger logger) {
		this.centralManager = agent;
		this.countOfReplaning = countOfReplaning;
		this.logger = logger;
	}
	
	public void run(Planner scheduler, JobRun jobRun) throws PlannerException {
		
		JobID jobID = jobRun.getJobID();
		
		// creating space in filesystem
		FilesystemTool.createLogSpaceForJobRun(jobID);
		
		if (jobID.getRunNumber() == 0) {
			FilesystemTool.moveInputJobToResultDir(jobID);
		}
		
		scheduler.agentInitialization(centralManager, jobRun, logger);
		
		long iterationNumI = 0;
		while (iterationNumI < countOfReplaning) {
			
			// log information about re-planning
			logger.log(Level.INFO, "Replanning: " + iterationNumI++ + " / " + countOfReplaning);
			
			// sleep
			try {
				Thread.sleep(Configuration.REPLAN_PERIOD_MS);
			} catch (InterruptedException e) {
				logger.logThrowable("Error by waiting for replan", e);
			}
			
			ResultOfComputingWrapper resultOfComputingAgents =
					ComputingAgentService.sendAccessesResult(centralManager, logger);
 			ResultOfComputing resultI =
 					getBestResult(jobRun, resultOfComputingAgents);
 			
			saveResult(resultI);
			
			// re-planning
			Iteration iterationStructI = new Iteration(iterationNumI, countOfReplaning);
			
			ReceivedData receivedData = new ReceivedData();
			receivedData.setResultOfComputingWrapper(resultOfComputingAgents);
			
			scheduler.replan(centralManager, jobRun, iterationStructI, receivedData, logger);
		}
		scheduler.exit(centralManager, logger);
		
	}
	
	private ResultOfComputing getBestResult(JobRun jobRun, ResultOfComputingWrapper resultOfComputingAgents) {
		
		Problem problem = jobRun.getProblem();
		return resultOfComputingAgents.exportBestResultOfComputing(problem);
	}
	
	private void saveResult(ResultOfComputing bestResult) {
		
		if (bestResult != null) {
			logger.log(Level.INFO, "" + bestResult.getFitnessValue());
		
			DataManagerService.sendResultOfComputing(centralManager, bestResult, logger);
		}
	}
	
}
