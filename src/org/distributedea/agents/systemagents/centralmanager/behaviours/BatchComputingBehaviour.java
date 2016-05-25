package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.util.List;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.SchedulerException;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.input.PostProcessing;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.job.Job;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;
import org.distributedea.problems.ProblemTool;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

public class BatchComputingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batch batch;
	private AgentLogger logger;
	
	public BatchComputingBehaviour(Batch batch, AgentLogger logger) {
		this.batch = batch;
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		List<JobWrapper> jobWrps = batch.getJobWrappers();
		String batchID = batch.getBatchID();
		
		for (JobWrapper jobWrpI : jobWrps) {
			
			try {
				processJobWrapper(jobWrpI, batchID);
			} catch (SchedulerException e) {
				logger.logThrowable("Can't execute Job", e);
			}
		}
	
		if (InputConfiguration.runPostProcessing) {
			
			List<PostProcessing> postProcessings = batch.getPostProcessings();
			for (PostProcessing postProcI : postProcessings) {
				
				try {
					processPostProcessing(postProcI, batch);
				} catch (SchedulerException e) {
					logger.logThrowable("Can't execute PostProcessing", e);
				}
			}
		}
	}
	
	private void processJobWrapper(JobWrapper jobWrp, String batchID) throws SchedulerException {
		
		String jobID = jobWrp.getJobID();
		int numberOfRuns = jobWrp.getNumberOfRuns();
		boolean individualDistribution = jobWrp.isIndividualDistribution();
		long countOfReplaning = jobWrp.getCountOfReplaning();
		Scheduler scheduler = jobWrp.getScheduler();
		//Class<?> problemToSolve = jobWrp.getProblemToSolve();
		String problemFileName = jobWrp.getProblemFileName();
		String methodsFileName = jobWrp.getMethodsFileName();
		ProblemTools problemTools = jobWrp.getProblemTools();
		
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
		
		

		for (int runNumberI = 0; runNumberI < numberOfRuns; runNumberI++) {
			
			Job job = new Job();
			job.setIndividualDistribution(individualDistribution);
			job.setAgentConfigurations(agentConfigurations);
			job.setProblem(problem);
			job.setProblemTools(problemTools);
			
			job.setJobID(new JobID(batchID, jobID, runNumberI));

			processJob(job, scheduler, countOfReplaning);
		}
		
	}
	
	private void processJob(Job job, Scheduler scheduler, long countOfReplaning) throws SchedulerException {
		
		JobComputingBehaviour jobBehaviour = new JobComputingBehaviour(job, scheduler, countOfReplaning, logger);
		this.myAgent.addBehaviour(jobBehaviour);
	}

	private void processPostProcessing(PostProcessing postProc, Batch batch) throws SchedulerException {
		
		Behaviour behaviour = new PostProcessingBehaviour(postProc, batch);
		this.myAgent.addBehaviour(behaviour);

	}
}
