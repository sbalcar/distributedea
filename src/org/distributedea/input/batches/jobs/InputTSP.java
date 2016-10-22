package org.distributedea.input.batches.jobs;


import java.io.File;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerInitialisationRunEachMethodOnce_;
import org.distributedea.agents.systemagents.centralmanager.planners.historybased.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.plannertype.PlannerTypeTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
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
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemTSPGPS.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("simpleTest.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
	
	public static Job test02() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("tsp02");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemTSPGPS.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("wi29.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}

	public static Job test03() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("tsp03");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemTSPPoint.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("djb2036.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolPoint2opt.class));
		
		return job;
	}
	
	public static Job test04() {
		
		InputConfiguration.automaticStart = true;
		
		Job job = new Job();
		job.setJobID("tsp04");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemTSPGPS.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerTheBestHelper());
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}

	public static Job test05() {
				
		Job job = new Job();
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemTSPGPS.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerType(new PlannerTypeTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
	
	public static Job test06() {
				
		Job job = new Job();
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(true);
		job.importProblemToSolve(ProblemTSPGPS.class);
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));

		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerType(new PlannerTypeTimeRestriction(10));
		job.setProblemTools(
				new ProblemTools(ProblemToolGPSEuc2D2opt.class));
		
		return job;
	}
}
