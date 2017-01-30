package org.distributedea.input.jobs;


import java.io.File;
import java.io.IOException;

import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerInitialisationRunEachMethodOnce_;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPoint2opt;

/**
 * Defines a set of TSP inputs
 */
public class InputTSP {
	
	public static Job test01() throws IOException {
		
		Job job = new Job();
		job.setJobID("tsp01");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("simpleTest.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
	
	public static Job test02() throws IOException {
		
		Job job = new Job();
		job.setJobID("tsp02");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("wi29.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}

	public static Job test03() throws IOException {
		
		Job job = new Job();
		job.setJobID("tsp03");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPPoint());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("djb2036.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolPoint2opt.class));
		
		return job;
	}
	
	public static Job test04() throws IOException {
		
		Job job = new Job();
		job.setJobID("tsp04");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerTheBestHelper());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}

	public static Job test05() throws IOException {
				
		Job job = new Job();
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
	
	public static Job test06() throws IOException {
				
		Job job = new Job();
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());

		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerTimeRestriction(10));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
}
