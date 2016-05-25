package org.distributedea.input.batches.jobs;


import org.distributedea.Configuration;
import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerInitialization;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerInitializationRunEachMethodOnce;
import org.distributedea.ontology.job.noontology.JobWrapper;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPoint2opt;

/**
 * Defines a set of TSP inputs
 */
public class InputTSP {
	
	public static JobWrapper test01() {
		
		InputConfiguration.automaticStart = true;
		
		JobWrapper job = new JobWrapper();
		job.setJobID("tsp01");
		job.setNumberOfRuns(1);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("simpleTest.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setScheduler(new SchedulerInitializationRunEachMethodOnce());
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
	
	public static JobWrapper test02() {
		
		InputConfiguration.automaticStart = true;
		
		JobWrapper job = new JobWrapper();
		job.setJobID("tsp02");
		job.setNumberOfRuns(1);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("wi29.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setScheduler(new SchedulerInitializationRunEachMethodOnce());
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}

	public static JobWrapper test03() {
		
		InputConfiguration.automaticStart = true;
		
		JobWrapper job = new JobWrapper();
		job.setJobID("tsp03");
		job.setNumberOfRuns(1);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPPoint.class);
		job.setProblemFileName("djb2036.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setScheduler(new SchedulerInitializationRunEachMethodOnce());
		job.setProblemTools(
				new ProblemTools(ProblemToolPoint2opt.class));
		
		return job;
	}
	
	public static JobWrapper test04() {
		
		InputConfiguration.automaticStart = true;
		
		JobWrapper job = new JobWrapper();
		job.setJobID("tsp04");
		job.setNumberOfRuns(1);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("xit1083.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setScheduler(new SchedulerFollowupHelpers());
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}

	public static JobWrapper test05() {
				
		JobWrapper job = new JobWrapper();
		job.setNumberOfRuns(3);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("xit1083.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setScheduler(new SchedulerInitialization());
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
	
	public static JobWrapper test06() {
				
		JobWrapper job = new JobWrapper();
		job.setNumberOfRuns(3);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("xit1083.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
}
