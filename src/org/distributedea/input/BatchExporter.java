package org.distributedea.input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.binpacking.objects1000.BatchSingleMethodsBPP1000;
import org.distributedea.input.batches.co.BatchHeteroComparingCO;
import org.distributedea.input.batches.co.f2.BatchSingleMethodsCOf2;
import org.distributedea.input.batches.tsp.BatchTestTSP;
import org.distributedea.input.batches.tsp.cities1083.BatchHeteroMethodsTSP1083;
import org.distributedea.input.batches.tsp.cities1083.BatchHomoMethodsTSP1083;
import org.distributedea.input.batches.tsp.cities1083.BatchSingleMethodsTSP1083;

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
		
		IInputBatch inputBatchTestTSP = new BatchTestTSP();
		Batch batchTestTSP = inputBatchTestTSP.batch();

		IInputBatch inputBatchHeteroCmpTSP = new BatchHeteroMethodsTSP1083(); 
		Batch batchHeteroCmpTSP = inputBatchHeteroCmpTSP.batch();
				
		IInputBatch inputBatchHomoCmpTSP = new BatchHomoMethodsTSP1083();
		Batch batchHomoCmpTSP = inputBatchHomoCmpTSP.batch();
				
		IInputBatch inputBatchHomoSimpleTestTSP = new BatchSingleMethodsTSP1083();
		Batch batchHomoSimpleTestTSP = inputBatchHomoSimpleTestTSP.batch();
		
		IInputBatch inputBatchSingleMethodsBPP1000 = new BatchSingleMethodsBPP1000();
		Batch singleMethodsBPP1000 = inputBatchSingleMethodsBPP1000.batch();
		
		IInputBatch inputBatchSingleMethodsCOf2 = new BatchSingleMethodsCOf2();
		Batch singleMethodsCOf2 = inputBatchSingleMethodsCOf2.batch();
		
		List<Batch> batchesList = new ArrayList<>();
		// TSP
		batchesList.add(batchHeteroCmpTSP);
		batchesList.add(batchHomoCmpTSP);
		batchesList.add(batchTestTSP);
		batchesList.add(batchHomoSimpleTestTSP);
		
		// BP
		batchesList.add(singleMethodsBPP1000);
		
		// CO
		batchesList.add(batchHeteroCmpCO);
		batchesList.add(singleMethodsCOf2);
		
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
