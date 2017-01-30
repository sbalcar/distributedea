package org.distributedea.input.jobs;

import java.io.File;
import java.io.IOException;

import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.problems.binpacking.permutation.ProblemToolBinPackingSimpleShift;

public class InputBinPacking {

	public static Job test01() throws IOException {
		
		Job job = new Job();
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemBinPacking(1));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("bp1000.bpp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)), new TrashLogger());
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		return job;
	}
}
