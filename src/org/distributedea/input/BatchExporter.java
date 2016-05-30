package org.distributedea.input;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.distributedea.input.batches.BatchHeteroComparingCO;
import org.distributedea.input.batches.BatchHeteroComparingTSP;
import org.distributedea.input.batches.BatchHomoComparingTSP;
import org.distributedea.input.batches.InputBatch;
import org.distributedea.ontology.job.noontology.Batch;

public class BatchExporter {
	
	public static void main(String [] args) throws FileNotFoundException, JAXBException {
		
		InputBatch ainputBatchHeteroCmpCO = new BatchHeteroComparingCO();
		Batch batchHeteroCmpCO = ainputBatchHeteroCmpCO.batch();		
		
		batchHeteroCmpCO.exportBatchToJobQueueDirectory();
		
		
		
		InputBatch inputBatchHeteroCmpTSP = new BatchHeteroComparingTSP(); 
		Batch batchHeteroCmpTSP = inputBatchHeteroCmpTSP.batch();
		
		batchHeteroCmpTSP.exportBatchToJobQueueDirectory();
		
		
		
		InputBatch inputBatchHomoCmpTSP = new BatchHomoComparingTSP();
		Batch batchHomoCmpTSP = inputBatchHomoCmpTSP.batch();
		
		batchHomoCmpTSP.exportBatchToJobQueueDirectory();
		
	}
}
