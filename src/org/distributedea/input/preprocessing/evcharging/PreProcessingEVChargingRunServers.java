package org.distributedea.input.preprocessing.evcharging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.input.preprocessing.PreProcessing;
import org.distributedea.problems.evcharging.server.ServerThread;

public class PreProcessingEVChargingRunServers extends PreProcessing {

	private int port;
	private int serverCount;
	private File datasetFile;
	
	public PreProcessingEVChargingRunServers(int port, int serverCount, File datasetFile) {
		this.port = port;
		this.serverCount = serverCount;
		this.datasetFile = datasetFile;
	}
	
	@Override
	public void run(Batch batch) throws Exception {
				
    	System.out.println(datasetFile.getAbsolutePath());

    	List<Thread> threads = new ArrayList<Thread>();
    	for (int i = 0; i < this.serverCount; i++) {
	    	Thread newThreadI = new ServerThread(datasetFile, port + i);
	    	newThreadI.start();
	    	threads.add(newThreadI);
    	}

    	Thread.sleep(2000);
    	
    	//for (Thread t : threads) {
    	//     t.stop();
    	//}
    	
    	Thread.sleep(30000);
	}

}
