package org.distributedea.agents.systemagents.centralmanager.behaviours;


import java.io.File;
import java.text.NumberFormat;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.agents.systemagents.datamanager.DataManagerService;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.computing.result.ResultOfComputingWrapper;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;

import jade.core.behaviours.OneShotBehaviour;

public class ComputeJobRunBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private JobRun jobRun;
	private Scheduler scheduler;
	private long countOfReplaning;
	private AgentLogger logger;
	
	public ComputeJobRunBehaviour(JobRun jobRun, Scheduler scheduler,
			long countOfReplaning, AgentLogger logger) {

		this.jobRun = jobRun;
		this.scheduler = scheduler;
		this.countOfReplaning = countOfReplaning;
		this.logger = logger;
	}
	
	@Override
	public void action() {

		// testing validity
		if (logger == null) {
			return;
		}
		
		if (scheduler == null) {
			logger.log(Level.WARNING, "scheduler can not be null");
			return;
		}

		if ((jobRun == null) || (! jobRun.validation())) {
			logger.log(Level.WARNING, "job can not be null");
			return;
		}


		logger.log(Level.INFO, "Computing JobRun behaviour: " + jobRun.getJobID());
		
		try {
			startComputing();
		} catch (SchedulerException e) {
			logger.logThrowable("Computing was stoped", e);
		}
		
	}

	
	protected void startComputing() throws SchedulerException {			
		
		JobID jobID = jobRun.getJobID();
		
		// creating space in filesystem
		FilesystemTool.createLogSpaceForJobRun(jobID);
		
		if (jobID.getRunNumber() == 0) {
			FilesystemTool.moveInputJobToResultDir(jobID);
		}
		
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		scheduler.agentInitialization(centralManager, jobRun, logger);
		
		long generation = 0;
		while (generation < countOfReplaning) {
			
			//printMemory();
			//printSpace();
			
			// sleep
			try {
				Thread.sleep(Configuration.REPLAN_PERIOD_MS);
			} catch (InterruptedException e) {
				logger.logThrowable("Error by waiting for replan", e);
			}
			
			ResultOfComputingWrapper resultOfComputingAgents =
					ComputingAgentService.sendAccessesResult(centralManager, logger);
			
			Problem problem = jobRun.getProblem();
 			ResultOfComputing resultI = resultOfComputingAgents.exportBestResultOfComputing(problem);
			saveResult(resultI);
			
			// log information about re-planning
			logger.log(Level.INFO, "Replanning: " + generation++ + " / " + countOfReplaning);
			
			// re-planning
			scheduler.replan(centralManager, jobRun, logger);
		}
		scheduler.exit(centralManager, logger);

	}
	

	
	private void saveResult(ResultOfComputing bestResult) {
		
		if (bestResult != null) {
			logger.log(Level.INFO, "" + bestResult.getFitnessValue());
		
			Agent_DistributedEA distAgent = (Agent_DistributedEA) this.myAgent;
			DataManagerService.sendResultOfComputing(distAgent, bestResult, logger);
		}
	}
	
	@SuppressWarnings("unused")
	private void printMemory() {
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		System.out.println("free memory: " + format.format(freeMemory / 1024) + "<br/>");
		System.out.println("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
		System.out.println("max memory: " + format.format(maxMemory / 1024) + "<br/>");
		System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
	}
	

	@SuppressWarnings("unused")
	private void printSpace() {
		
        File diskPartition = new File("~/");
        long totalCapacity = diskPartition.getTotalSpace();
        long freePartitionSpace = diskPartition.getFreeSpace();
        long usablePatitionSpace = diskPartition.getUsableSpace(); 

        System.out.println("totalCapacity: " + totalCapacity);
        System.out.println("freePartitionSpace: " + freePartitionSpace);
        System.out.println("usablePatitionSpace: " + usablePatitionSpace);
	}


}
