package org.distributedea.input.batches;

import org.distributedea.Configuration;
import org.distributedea.input.batches.jobs.InputTSP;
import org.distributedea.input.batches.jobs.MethodConstants;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcComparing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;

public class BatchHomoComparingTSP extends InputBatch {

	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("homoComparingTSP");
		batch.setDescription("Porovnání homogeních modelů : TSP / Planovani = incializace");
		
		Job jobW0 = InputTSP.test05();
		jobW0.setJobID("homoHillclimbing");
		jobW0.setDescription("Homo-HillClimbing");
		jobW0.setMethodsFileName(Configuration.getMethodsFile(
				MethodConstants.METHODS_ONLY_HILLCLIMBING));
		
		Job jobW1 = InputTSP.test05();
		jobW1.setJobID("homoRandomsearch");
		jobW1.setDescription("Homo-RandomSearch");
		jobW1.setMethodsFileName(Configuration.getMethodsFile(
				MethodConstants.METHODS_ONLY_RANDOMSEARCH));
		
		Job jobW2 = InputTSP.test05();
		jobW2.setJobID("homoEvolution");
		jobW2.setDescription("Homo-Evolution");
		jobW2.setMethodsFileName(Configuration.getMethodsFile(
				MethodConstants.METHODS_ONLY_EVOLUTION));
		
		Job jobW3 = InputTSP.test05();
		jobW3.setJobID("homoBruteforce");
		jobW3.setDescription("Homo-BruteForce");
		jobW3.setMethodsFileName(Configuration.getMethodsFile(
				MethodConstants.METHODS_ONLY_BRUTEFORCE));
		
		Job jobW4 = InputTSP.test05();
		jobW4.setJobID("homoTabusearch");
		jobW4.setDescription("Homo-TabuSearch");
		jobW4.setMethodsFileName(Configuration.getMethodsFile(
				MethodConstants.METHODS_ONLY_TABUSEARCH));
		
		Job jobW5 = InputTSP.test05();
		jobW5.setJobID("homoSimulatedannealing");
		jobW5.setDescription("Homo-SimulatedAnnealing");
		jobW5.setMethodsFileName(Configuration.getMethodsFile(
				MethodConstants.METHODS_ONLY_SIMULATEDANNEALING));
		
		Job jobW6 = InputTSP.test05();
		jobW6.setJobID("homoDifferentialevolution");
		jobW6.setDescription("Homo-DifferentialEvolution");
		jobW6.setMethodsFileName(Configuration.getMethodsFile(
				MethodConstants.METHODS_ONLY_DIFFEVOLUTION));
		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW3);
		batch.addJobWrapper(jobW4);
		batch.addJobWrapper(jobW5);
		batch.addJobWrapper(jobW6);
		
		
		PostProcessing psMat0 = new PostProcBoxplot();
		PostProcessing psMat1 = new PostProcComparing();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcBatchDiffTable();
		PostProcessing psLat1 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		return batch;
	}
	
}
