package org.distributedea.input.batches;

import org.distributedea.InputTSP;
import org.distributedea.input.InputBatch;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

public class BatchHomoComparing extends InputBatch {

	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("batch2");
		batch.setDescription("Porovnání homogeních modelů : TSP / Planovani = incializace");
		
		JobWrapper jobW0 = InputTSP.test05(0);
		jobW0.setDescription("Homo-HillClimbing");
		
		JobWrapper jobW1 = InputTSP.test05(1);
		jobW1.setDescription("Homo-RandomSearch");
		
		JobWrapper jobW2 = InputTSP.test05(2);
		jobW2.setDescription("Homo-Evolution");
		
		JobWrapper jobW3 = InputTSP.test05(3);
		jobW3.setDescription("Homo-BruteForce");
		
		JobWrapper jobW4 = InputTSP.test05(4);
		jobW4.setDescription("Homo-TabuSearch");

		JobWrapper jobW5 = InputTSP.test05(5);
		jobW5.setDescription("Homo-SimulatedAnnealing");

		JobWrapper jobW6 = InputTSP.test05(6);
		jobW6.setDescription("Homo-DifferentialEvolution");

		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW3);
		batch.addJobWrapper(jobW4);
		batch.addJobWrapper(jobW5);
		batch.addJobWrapper(jobW6);
		
		return batch;
	}
	
}
