package org.distributedea;


import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerDummy;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerInitializationRunOneSeveralTimes;
import org.distributedea.ontology.job.noontology.JobWrapper;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;
import org.distributedea.problems.continuousoptimization.ProblemToolRandomMove;

/**
 * Defines a set of Continuous Optimization
 */
public class InputContOpt {

	public static JobWrapper test01() {
		
		InputConfiguration.automaticStart = true;
		
		JobWrapper job = new JobWrapper();
		job.setJobID("co01");
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(false);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName("f01.co");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(new SchedulerDummy());
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));
		
		return job;
	}
	
	public static JobWrapper test02() {
		
		InputConfiguration.automaticStart = true;
		
		JobWrapper job = new JobWrapper();
		job.setJobID("co02");
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName(Configuration.getInputProblemFile("f01.co"));
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(new SchedulerFollowupHelpers());
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));
		
		return job;
	}
	
	public static JobWrapper test03() {
		
		InputConfiguration.automaticStart = true;
		
		JobWrapper job = new JobWrapper();
		job.setJobID("co03");
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName("f01.co");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(new SchedulerInitializationRunOneSeveralTimes(0, 0));
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));

		return job;
	}
}
