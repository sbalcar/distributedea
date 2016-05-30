package org.distributedea.tests.jobqueue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.InputJobQueue;
import org.distributedea.input.batches.BatchHeteroComparingTSP;
import org.distributedea.input.batches.InputBatch;
import org.distributedea.ontology.job.noontology.Batch;

public class Test {

	public static void test() {
		
		InputBatch inputBatch = new BatchHeteroComparingTSP();
		Batch batch = inputBatch.batch();
		
		try {
			batch.exportBatchToJobQueueDirectory();
		} catch (FileNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
		
		List<Batch> importedBatches = null;
		try {
			importedBatches = InputJobQueue.getInputBatches();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Batch importedBatch = importedBatches.get(0);
		
		System.out.println(importedBatch.getBatchID());
		System.out.println(importedBatch.getDescription());
		System.out.println(importedBatch.getPostProcessings().size());
	}
	
	public static void main(String [] args) {
		
		test();
	}
}
