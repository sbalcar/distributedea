package org.distributedea.input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.binpacking.objects1000.BatchHeteroMethodsBPP1000;
import org.distributedea.input.batches.binpacking.objects1000.BatchHomoMethodsBPP1000;
import org.distributedea.input.batches.binpacking.objects1000.BatchSingleMethodsBPP1000;
import org.distributedea.input.batches.co.f2.BatchHeteroMethodsCOf2;
import org.distributedea.input.batches.co.f2.BatchHomoMethodsCOf2;
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
		
		IInputBatch inputBatchTestTSP = new BatchTestTSP();
		Batch batchTestTSP = inputBatchTestTSP.batch();
		
		IInputBatch inputBatchSimpleMethodsTSP1083 = new BatchSingleMethodsTSP1083();
		Batch batchSimpleMethodsTSP1083 = inputBatchSimpleMethodsTSP1083.batch();
		
		IInputBatch inputBatchHomoTSP1083 = new BatchHomoMethodsTSP1083();
		Batch batchHomoTSP1083 = inputBatchHomoTSP1083.batch();
		
		IInputBatch inputBatchHeteroTSP1083 = new BatchHeteroMethodsTSP1083(); 
		Batch batchHeteroTSP1083 = inputBatchHeteroTSP1083.batch();
		
		
		IInputBatch inputBatchSingleMethodsBPP1000 = new BatchSingleMethodsBPP1000();
		Batch batchSingleMethodsBPP1000 = inputBatchSingleMethodsBPP1000.batch();

		IInputBatch inputBatchHomoBPP1000 = new BatchHomoMethodsBPP1000();
		Batch batchHomoBPP1000 = inputBatchHomoBPP1000.batch();

		IInputBatch inputBatchHeteroBPP1000 = new BatchHeteroMethodsBPP1000(); 
		Batch batchHeteroBPP1000 = inputBatchHeteroBPP1000.batch();

		
		IInputBatch inputBatchSingleMethodsCOf2 = new BatchSingleMethodsCOf2();
		Batch batchSingleMethodsCOf2 = inputBatchSingleMethodsCOf2.batch();

		IInputBatch inputBatchHomoCOF2 = new BatchHomoMethodsCOf2();
		Batch batchHomoCOF2 = inputBatchHomoCOF2.batch();

		IInputBatch inputBatchHeteroCOF2 = new BatchHeteroMethodsCOf2();
		Batch batchHeteroCOF2 = inputBatchHeteroCOF2.batch();
		
		
		List<Batch> batchesList = new ArrayList<>();
		
		// TSP
		batchesList.add(batchTestTSP);
		
		batchesList.add(batchSimpleMethodsTSP1083);
		batchesList.add(batchHomoTSP1083);
		batchesList.add(batchHeteroTSP1083);
		
		// BP
		batchesList.add(batchSingleMethodsBPP1000);
		batchesList.add(batchHomoBPP1000);
		batchesList.add(batchHeteroBPP1000);
		
		// CO
		batchesList.add(batchSingleMethodsCOf2);
		batchesList.add(batchHomoCOF2);
		batchesList.add(batchHeteroCOF2);
		
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
