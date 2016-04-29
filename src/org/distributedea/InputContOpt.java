package org.distributedea;

import java.util.ArrayList;
import java.util.Arrays;

import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerDummy;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerRunOneSeveralTimes;
import org.distributedea.ontology.job.Job;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.problems.continuousoptimization.ProblemToolRandomMove;

/**
 * Defines a set of Continuous Optimization
 */
public class InputContOpt {

	public Job test01() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co01");
		job.setIndividualDistribution(false);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName("f01.co");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(SchedulerDummy.class);
		job.setAvailableProblemTools(new ArrayList<Class<?>>(
			    Arrays.asList(ProblemToolRandomMove.class)));
		
		return job;
	}
	
	public Job test02() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co02");
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName(Configuration.getInputProblemFile("f01.co"));
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(SchedulerFollowupHelpers.class);
		job.setAvailableProblemTools(new ArrayList<Class<?>>(
			    Arrays.asList(ProblemToolRandomMove.class)));
		
		return job;
	}
	
	public Job test03() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co03");
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName("f01.co");
		job.setMethodsFileName(Configuration.getMethodsFile());
		
		job.setScheduler(SchedulerRunOneSeveralTimes.class);
		job.setAvailableProblemTools(new ArrayList<Class<?>>(
			    Arrays.asList(ProblemToolRandomMove.class)));

		return job;
	}
}
