package org.distributedea.tests.history;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.InputJobQueue;
import org.distributedea.input.postprocessing.matlab.PostProcMonitoring;
import org.distributedea.ontology.job.noontology.Batch;

public class Test {

	public static void main(String [] args) throws FileNotFoundException {
				
		List<Batch> batches = null;
		try {
			batches = InputJobQueue.getInputBatches();
		} catch (IOException e) {
			//getLogger().log(Level.INFO, "Can not load input jobs");
		}
		
		Batch batch = batches.get(0);
		
		PostProcMonitoring pp = new PostProcMonitoring();
		pp.run(batch);
		

		
	}
	
}

