package org.distributedea.tests.history;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.postprocessing.matlab.dumy.PostProcMonitoring;

public class Test {

	public static void main(String [] args) throws Exception {
				
		List<Batch> batches = getInputBatches();
		
		
		Batch batch = batches.get(0);
		
		PostProcMonitoring pp = new PostProcMonitoring();
		pp.run(batch);
		
	}
	
	private static List<Batch> getInputBatches() throws IOException {
		
		File batchesDir = new File(FileNames.getDirectoryOfInputBatches());
		
		List<Batch> batches = new ArrayList<>();

		for (File batchFileI : batchesDir.listFiles()) {
			if (batchFileI.isDirectory()) {
				Batch batchI = Batch.importXML(batchFileI);
				batches.add(batchI);
			}
		}
		
		return batches;
	}
}

