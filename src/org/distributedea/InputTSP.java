package org.distributedea;

import java.util.ArrayList;
import java.util.Arrays;

import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerRunEachMethodOnce;
import org.distributedea.ontology.job.Job;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPoint2opt;

/**
 * Defines a set of TSP inputs
 */
public class InputTSP {

	public static Job test01() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("tsp01");
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("simpleTest.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(SchedulerRunEachMethodOnce.class);
		job.setAvailableProblemTools(new ArrayList<Class<?>>(
			    Arrays.asList(ProblemToolGPSEuc2D2opt.class)));
		
		return job;
	}
	
	public static Job test02() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("tsp02");
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("wi29.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		//job.setScheduler(SchedulerRunEachMethodOnce.class);
		job.setScheduler(SchedulerFollowupHelpers.class);
		job.setAvailableProblemTools(new ArrayList<Class<?>>(
			    Arrays.asList(ProblemToolGPSEuc2D2opt.class)));
		
		return job;
	}

	public static Job test03() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("tsp03");
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPPoint.class);
		job.setProblemFileName("djb2036.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(SchedulerRunEachMethodOnce.class);
		job.setAvailableProblemTools(new ArrayList<Class<?>>(
			    Arrays.asList(ProblemToolPoint2opt.class)));
		
		return job;
	}
	
	public static Job test04() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("tsp04");
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemTSPGPS.class);
		job.setProblemFileName("xit1083.tsp");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(SchedulerFollowupHelpers.class);
		job.setAvailableProblemTools(new ArrayList<Class<?>>(
			    Arrays.asList(ProblemToolGPSEuc2D2opt.class)));
		
		return job;
	}
	
}
