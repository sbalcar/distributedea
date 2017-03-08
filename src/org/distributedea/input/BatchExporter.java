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
import org.distributedea.input.batches.co.bbob04.BatchHeteroMethodsCOf04;
import org.distributedea.input.batches.co.bbob04.BatchHomoMethodsCOf04;
import org.distributedea.input.batches.co.bbob04.BatchSingleMethodsCOf04;
import org.distributedea.input.batches.co.bbobf08.BatchHeteroMethodsCOf08;
import org.distributedea.input.batches.co.bbobf08.BatchHomoMethodsCOf08;
import org.distributedea.input.batches.co.bbobf08.BatchSingleMethodsCOf08;
import org.distributedea.input.batches.co.f2.BatchHeteroMethodsCOf2;
import org.distributedea.input.batches.co.f2.BatchHomoMethodsCOf2;
import org.distributedea.input.batches.co.f2.BatchSingleMethodsCOf2;
import org.distributedea.input.batches.machinelearning.zoo.BatchHeteroMethodsMLZoo;
import org.distributedea.input.batches.machinelearning.zoo.BatchHomoMethodsMLZoo;
import org.distributedea.input.batches.machinelearning.zoo.BatchSingleMethodsMLZoo;
import org.distributedea.input.batches.tsp.cities1083.BatchHeteroMethodsTSP1083;
import org.distributedea.input.batches.tsp.cities1083.BatchHomoMethodsTSP1083;
import org.distributedea.input.batches.tsp.cities1083.BatchSingleMethodsTSP1083;

/**
 * Generates Input {@link Batch}es to directory "batches". For running Batch
 * user copy Batch from this directory to queue directory. 
 * @author stepan
 *
 */
public class BatchExporter {
	
	public static void main(String [] args) throws Exception {		
		
		//IInputBatch inputBatchTestTSP = new BatchTestTSP();
		//Batch batchTestTSP = inputBatchTestTSP.batch();
		
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
		

		IInputBatch inputBatchSingleMethodsCOf04 = new BatchSingleMethodsCOf04();
		Batch batchSingleMethodsCOf04 = inputBatchSingleMethodsCOf04.batch();

		IInputBatch inputBatchHomoMethodsCOf04 = new BatchHomoMethodsCOf04();
		Batch batchHomoMethodsCOf04 = inputBatchHomoMethodsCOf04.batch();

		IInputBatch inputBatchHeteroMethodsCOf04 = new BatchHeteroMethodsCOf04();
		Batch batchHeteroMethodsCOf04 = inputBatchHeteroMethodsCOf04.batch();
		
		
		IInputBatch inputBatchSingleMethodsCOf08 = new BatchSingleMethodsCOf08();
		Batch batchSingleMethodsCOf08 = inputBatchSingleMethodsCOf08.batch();

		IInputBatch inputBatchHomoMethodsCOf08 = new BatchHomoMethodsCOf08();
		Batch batchHomoMethodsCOf08 = inputBatchHomoMethodsCOf08.batch();

		IInputBatch inputBatchHeteroMethodsCOf08 = new BatchHeteroMethodsCOf08();
		Batch batchHeteroMethodsCOf08 = inputBatchHeteroMethodsCOf08.batch();

		
		IInputBatch inputBatchSingleMethodsMLZoo = new BatchSingleMethodsMLZoo();
		Batch batchSingleMethodsMLZoo = inputBatchSingleMethodsMLZoo.batch();

		IInputBatch inputBatchHomoMethodsMLZoo = new BatchHomoMethodsMLZoo();
		Batch batchHomoMethodsMLZoo = inputBatchHomoMethodsMLZoo.batch();

		IInputBatch inputBatchHeteroMethodsMLZoo = new BatchHeteroMethodsMLZoo();
		Batch batchHeteroMethodsMLZoo = inputBatchHeteroMethodsMLZoo.batch();

		
		List<Batch> batchesList = new ArrayList<>();
		
		// TSP
		//batchesList.add(batchTestTSP);
		
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

		batchesList.add(batchSingleMethodsCOf04);
		batchesList.add(batchHomoMethodsCOf04);
		batchesList.add(batchHeteroMethodsCOf04);
		
		batchesList.add(batchSingleMethodsCOf08);
		batchesList.add(batchHomoMethodsCOf08);
		batchesList.add(batchHeteroMethodsCOf08);
		
		// ML
		batchesList.add(batchSingleMethodsMLZoo);
		batchesList.add(batchHomoMethodsMLZoo);
		batchesList.add(batchHeteroMethodsMLZoo);
		
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
