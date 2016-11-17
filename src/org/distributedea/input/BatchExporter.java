package org.distributedea.input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.BatchHeteroComparingCO;
import org.distributedea.input.batches.BatchHeteroComparingTSP;
import org.distributedea.input.batches.BatchHomoComparingTSP;
import org.distributedea.input.batches.BatchTestTSP;
import org.distributedea.input.batches.IInputBatch;

/**
 * Generates Input-Batches to directory "batches". For running Batch
 * user copy Batch from this directory to queue directory. 
 * @author stepan
 *
 */
public class BatchExporter {
	
	public static void main(String [] args) throws Exception {
		
		IInputBatch ainputBatchHeteroCmpCO = new BatchHeteroComparingCO();
		Batch batchHeteroCmpCO = ainputBatchHeteroCmpCO.batch();		
		
		IInputBatch inputBatchHeteroCmpTSP = new BatchHeteroComparingTSP(); 
		Batch batchHeteroCmpTSP = inputBatchHeteroCmpTSP.batch();
				
		IInputBatch inputBatchHomoCmpTSP = new BatchHomoComparingTSP();
		Batch batchHomoCmpTSP = inputBatchHomoCmpTSP.batch();
		
		IInputBatch inputBatchTestTSP = new BatchTestTSP();
		Batch batchTestTSP = inputBatchTestTSP.batch();
		
		List<Batch> batchesList = new ArrayList<>();
		batchesList.add(batchHeteroCmpCO);
		batchesList.add(batchHeteroCmpTSP);
		batchesList.add(batchHomoCmpTSP);
		batchesList.add(batchTestTSP);
		
		
		// creates directory if doesn't exist
		File batchesDir = new File(FileNames.getDirectoryOfBatches());
		if (! batchesDir.exists()) {
			batchesDir.mkdir();
		}
		
		
		Batches batches = new Batches(batchesList);
		batches.exportXML(batchesDir);
		
		Batches.importXML(batchesDir);
	}
	
}
