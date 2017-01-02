package org.distributedea.input.batches.tsp.cities1083;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputTSP;
import org.distributedea.input.jobs.MethodConstants;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobRunsResultTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;

public class BatchHomoMethodsTSP1083 implements IInputBatch {

	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("homoMethodsTSP1083");
		batch.setDescription("Porovnání homogeních modelů : TSP1083");
		
		Job jobW0 = InputTSP.test05();
		jobW0.setJobID("homoHillclimbing");
		jobW0.setDescription("Homo-HillClimbing");
		jobW0.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_HILLCLIMBING)));
		
		Job jobW1 = InputTSP.test05();
		jobW1.setJobID("homoRandomsearch");
		jobW1.setDescription("Homo-RandomSearch");
		jobW1.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_RANDOMSEARCH)));
		
		Job jobW2 = InputTSP.test05();
		jobW2.setJobID("homoEvolution");
		jobW2.setDescription("Homo-Evolution");
		jobW2.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_EVOLUTION)));
		
		Job jobW3 = InputTSP.test05();
		jobW3.setJobID("homoBruteforce");
		jobW3.setDescription("Homo-BruteForce");
		jobW3.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_BRUTEFORCE)));
		
		Job jobW4 = InputTSP.test05();
		jobW4.setJobID("homoTabusearch");
		jobW4.setDescription("Homo-TabuSearch");
		jobW4.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_TABUSEARCH)));
		
		Job jobW5 = InputTSP.test05();
		jobW5.setJobID("homoSimulatedannealing");
		jobW5.setDescription("Homo-SimulatedAnnealing");
		jobW5.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_SIMULATEDANNEALING)));
		
		Job jobW6 = InputTSP.test05();
		jobW6.setJobID("homoDifferentialevolution");
		jobW6.setDescription("Homo-DifferentialEvolution");
		jobW6.importMethodsFile(new File(FileNames.getMethodsFile(
				MethodConstants.METHODS_ONLY_DIFFEVOLUTION)));
		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW3);
		batch.addJobWrapper(jobW4);
		batch.addJobWrapper(jobW5);
		batch.addJobWrapper(jobW6);
		
		
		PostProcessing psMat0 = new PostProcBoxplot();
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcJobRunsResultTable();
		PostProcessing psLat1 = new PostProcBatchDiffTable();
		PostProcessing psLat2 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		batch.addPostProcessings(psLat2);
		
		return batch;
	}
	
}
