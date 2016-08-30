package org.distributedea.tests.batches;

import java.io.File;
import java.io.IOException;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.BatchHeteroComparingTSP;
import org.distributedea.input.batches.InputBatch;

public class Test {

	public static void test() {
		
		InputBatch inputBatch = new BatchHeteroComparingTSP();
		Batch batch = inputBatch.batch();
		
		Batches batches = new Batches(batch);
		File batchDir = new File(FileNames.getDirectoryOfInputBatches());		
		
		try {
			batches.exportXML(batchDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File batchesDir = new File(FileNames.getDirectoryOfInputBatches());
			
		Batches importedBatches = null;
		try {
			importedBatches = Batches.importXML(batchesDir);
		} catch (IOException e) {
		}
		
		Batch importedBatch = importedBatches.getBatches().get(0);
		
		System.out.println(importedBatch.getBatchID());
		System.out.println(importedBatch.getDescription());
		System.out.println(importedBatch.getPostProcessings().size());
	}
	
	public static void main(String [] args) {
		
		test();
	}
}
