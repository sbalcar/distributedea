package org.distributedea.input.jobs;


import java.io.File;
import java.io.IOException;

import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerDummy;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problems.continuousoptimization.ProblemToolCORandomMove;

/**
 * Defines a set of Continuous Optimization
 */
public class InputContOpt {

	public static Job test01() throws IOException {
		
		Job job = new Job();
		job.setJobID("co01");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(false);
		job.setProblem(new ProblemContinuousOpt(true));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("f01.co")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerDummy());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolCORandomMove.class));
		
		return job;
	}
	
	public static Job test02() throws IOException {
		
		Job job = new Job();
		job.setJobID("co02");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemContinuousOpt(true));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("f01.co")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerTheBestHelper());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolCORandomMove.class));
		
		return job;
	}
	
	public static Job test03() throws IOException {
		
		Job job = new Job();
		job.setJobID("co03");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemContinuousOpt(true));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("f01.co")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolCORandomMove.class));

		return job;
	}
	
	public static Job test04() throws IOException {
		
		Job job = new Job();
		job.setJobID("f2");
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(false);
		job.setProblem(new ProblemContinuousOpt(false));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("f2.co")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerDummy());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolCORandomMove.class));
		
		return job;
	}
}
