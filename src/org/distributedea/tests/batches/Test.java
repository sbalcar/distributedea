package org.distributedea.tests.batches;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.tsp.cities1083.BatchHeteroMethodsTSP1083;

public class Test {

	public static void test() throws Exception {
		
		IInputBatch inputBatch = new BatchHeteroMethodsTSP1083();
		Batch batch = inputBatch.batch();
		
		Batches batches = new Batches(batch);
		File batchDir = new File(FileNames.getDirectoryOfInputBatches());		
		
		try {
			batches.exportXML(batchDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File batchesDir = new File(FileNames.getDirectoryOfInputBatches());
			
		Batches importedBatches = null;
		try {
			importedBatches = Batches.importXML(batchesDir);
		} catch (Exception e) {
		}
		
		Batch importedBatch = importedBatches.getBatches().get(0);
		
		System.out.println(importedBatch.getBatchID());
		System.out.println(importedBatch.getDescription());
		System.out.println(importedBatch.getPostProcessings().size());
	}
	
	public static void main(String [] args) throws Exception {
		
		test();
	}
}
