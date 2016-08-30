package org.distributedea.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.BatchHeteroComparingCO;
import org.distributedea.input.batches.BatchHeteroComparingTSP;
import org.distributedea.input.batches.BatchHomoComparingTSP;
import org.distributedea.input.batches.BatchTestTSP;
import org.distributedea.input.batches.InputBatch;

public class BatchExporter {
	
	public static void main(String [] args) throws IOException {
		
		InputBatch ainputBatchHeteroCmpCO = new BatchHeteroComparingCO();
		Batch batchHeteroCmpCO = ainputBatchHeteroCmpCO.batch();		
		
		InputBatch inputBatchHeteroCmpTSP = new BatchHeteroComparingTSP(); 
		Batch batchHeteroCmpTSP = inputBatchHeteroCmpTSP.batch();
				
		InputBatch inputBatchHomoCmpTSP = new BatchHomoComparingTSP();
		Batch batchHomoCmpTSP = inputBatchHomoCmpTSP.batch();
		
		InputBatch inputBatchTestTSP = new BatchTestTSP();
		Batch batchTestTSP = inputBatchTestTSP.batch();
		
		List<Batch> batchesList = new ArrayList<>();
		batchesList.add(batchHeteroCmpCO);
		batchesList.add(batchHeteroCmpTSP);
		batchesList.add(batchHomoCmpTSP);
		batchesList.add(batchTestTSP);
		
		File batchesDir = new File(FileNames.getDirectoryOfBatches());
		
		
		Batches batches = new Batches(batchesList);
		batches.exportXML(batchesDir);
		
	}
	
}
