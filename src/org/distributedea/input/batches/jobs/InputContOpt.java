package org.distributedea.input.batches.jobs;


import java.io.File;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerDummy;
import org.distributedea.agents.systemagents.centralmanager.planners.historybased.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.plannertype.PlannerTypeTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.problem.ProblemContinousOpt;
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
		job.setIndividualDistribution(false);
		job.importProblemToSolve(ProblemContinousOpt.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("f01.co")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerDummy());
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));
		
		return job;
	}
	
	public static Job test02() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co02");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemContinousOpt.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("f01.co")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerTheBestHelper());
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));
		
		return job;
	}
	
	public static Job test03() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("co03");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemContinousOpt.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("f01.co")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		job.setPlanner(new PlannerInitialisation(state, true));
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolRandomMove.class));

		return job;
	}
}
