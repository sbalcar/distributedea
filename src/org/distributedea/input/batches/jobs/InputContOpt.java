package org.distributedea.input.batches.jobs;


import org.distributedea.Configuration;
import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.centralmanager.planner.PlannerDummy;
import org.distributedea.agents.systemagents.centralmanager.planner.PlannerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitialization;
import org.distributedea.agents.systemagents.centralmanager.planner.initialization.PlannerInitializationState;
import org.distributedea.ontology.job.noontology.Job;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;
import org.distributedea.problems.continuousoptimization.ProblemToolRandomMove;

/**
 * Defines a set of Continuous Optimization
 */
public class InputContOpt {

	public static Job test01() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co01");
		job.setNumberOfRuns(1);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(false);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName(Configuration.getInputProblemFile("f01.co"));
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setPlanner(new PlannerDummy());
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));
		
		return job;
	}
	
	public static Job test02() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co02");
		job.setNumberOfRuns(1);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName(Configuration.getInputProblemFile("f01.co"));
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		job.setPlanner(new PlannerFollowupHelpers());
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));
		
		return job;
	}
	
	public static Job test03() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co03");
		job.setNumberOfRuns(1);
		job.setCountOfReplaning(50);
		job.setIndividualDistribution(true);
		job.setProblemToSolve(ProblemContinousOpt.class);
		job.setProblemFileName(Configuration.getInputProblemFile("f01.co"));
		job.setMethodsFileName(Configuration.getMethodsFile(MethodConstants.METHODS_ALL));
		
		PlannerInitializationState state = PlannerInitializationState.RUN_ONE_AGENT_PER_CORE;
		job.setPlanner(new PlannerInitialization(state, true));
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));

		return job;
	}
}
