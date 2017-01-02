package org.distributedea.input.jobs;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.problemdefinition.ProblemBinPackingDef;
import org.distributedea.problems.binpacking.permutation.ProblemToolBinPackingSimpleShift;

public class InputBinPacking {

	public static Job test01() {
		
		Job job = new Job();
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(true);
		job.setProblemDefinition(new ProblemBinPackingDef(1));
		job.importProblemFile(new File(
				FileNames.getInputProblemFile("bp1000.bpp")));
		job.importMethodsFile(new File(
				FileNames.getMethodsFile(MethodConstants.METHODS_ALL)));
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		job.setProblemTools(
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		return job;
	}
}
