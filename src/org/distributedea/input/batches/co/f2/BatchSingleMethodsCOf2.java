package org.distributedea.input.batches.co.f2;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputContOpt;
import org.distributedea.input.jobs.MethodConstants;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcJobRunsResultTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;

public class BatchSingleMethodsCOf2 implements IInputBatch {

	@Override
	public Batch batch() {

		Batch batch = new Batch();
		batch.setBatchID("singleMethodsCOf2");
		batch.setDescription("Porovnání samostatných metod : COf2");
		
		Job jobI = InputContOpt.test04();
		jobI.setJobID("0");
		jobI.setDescription("");
		jobI.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		
		Job job0 = jobI.deepClone();
		job0.setJobID("singleHillclimbing");
		job0.setDescription("Single-HillClimbing");
		job0.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_HILLCLIMBING)));
		
		Job job1 = jobI.deepClone();
		job1.setJobID("singleRandomsearch");
		job1.setDescription("Single-RandomSearch");
		job1.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_RANDOMSEARCH)));
		
		Job job2 = jobI.deepClone();
		job2.setJobID("singleEvolution");
		job2.setDescription("Single-Evolution");
		job2.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_EVOLUTION)));
		
		Job job3 = jobI.deepClone();
		job3.setJobID("singleBruteforce");
		job3.setDescription("Single-BruteForce");
		job3.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_BRUTEFORCE)));
		
		Job job4 = jobI.deepClone();
		job4.setJobID("singleTabusearch");
		job4.setDescription("Single-TabuSearch");
		job4.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_TABUSEARCH)));
		
		Job job5 = jobI.deepClone();
		job5.setJobID("singleSimulatedannealing");
		job5.setDescription("Single-SimulatedAnnealing");
		job5.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_SIMULATEDANNEALING)));
		
		Job job6 = jobI.deepClone();
		job6.setJobID("singleDifferentialevolution");
		job6.setDescription("Single-DifferentialEvolution");
		job6.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_DIFFEVOLUTION)));
		

		PostProcessing ps0 = new PostProcBoxplot();
		PostProcessing ps1 = new PostProcInvestigationOfMedianJobRun();

		PostProcessing psLat0 = new PostProcJobRunsResultTable();
		
		batch.addJobWrapper(job0);
		batch.addJobWrapper(job1);
		batch.addJobWrapper(job2);
		batch.addJobWrapper(job3);
		batch.addJobWrapper(job4);
		batch.addJobWrapper(job5);
		batch.addJobWrapper(job6);

		batch.addPostProcessings(ps0);
		batch.addPostProcessings(ps1);
		
		batch.addPostProcessings(psLat0);
		
		return batch;
	}


}
