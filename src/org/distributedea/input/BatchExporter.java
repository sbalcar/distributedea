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
import org.distributedea.input.batches.co.bbobf02.BatchHeteroMethodsCOf02;
import org.distributedea.input.batches.co.bbobf02.BatchHomoMethodsCOf02;
import org.distributedea.input.batches.co.bbobf02.BatchSingleMethodsCOf02;
import org.distributedea.input.batches.co.bbobf04.BatchHeteroMethodsCOf04;
import org.distributedea.input.batches.co.bbobf04.BatchHomoMethodsCOf04;
import org.distributedea.input.batches.co.bbobf04.BatchSingleMethodsCOf04;
import org.distributedea.input.batches.co.bbobf08.BatchHeteroMethodsCOf08;
import org.distributedea.input.batches.co.bbobf08.BatchHomoMethodsCOf08;
import org.distributedea.input.batches.co.bbobf08.BatchSingleMethodsCOf08;
import org.distributedea.input.batches.co.bbobf10.BatchHeteroMethodsCOf10;
import org.distributedea.input.batches.co.bbobf10.BatchHomoMethodsCOf10;
import org.distributedea.input.batches.co.bbobf10.BatchSingleMethodsCOf10;
import org.distributedea.input.batches.co.bbobf14.BatchHeteroMethodsCOf14;
import org.distributedea.input.batches.co.bbobf14.BatchHomoMethodsCOf14;
import org.distributedea.input.batches.co.bbobf14.BatchSingleMethodsCOf14;
import org.distributedea.input.batches.co.bbobf17.BatchHeteroMethodsCOf17;
import org.distributedea.input.batches.co.bbobf17.BatchHomoMethodsCOf17;
import org.distributedea.input.batches.co.bbobf17.BatchSingleMethodsCOf17;
import org.distributedea.input.batches.co.f2.BatchHeteroMethodsCOf2;
import org.distributedea.input.batches.co.f2.BatchHomoMethodsCOf2;
import org.distributedea.input.batches.co.f2.BatchSingleMethodsCOf2;
import org.distributedea.input.batches.machinelearning.wilt.BatchHeteroMethodsMLWilt;
import org.distributedea.input.batches.machinelearning.wilt.BatchHomoMethodsMLWilt;
import org.distributedea.input.batches.machinelearning.wilt.BatchSingleMethodsMLWilt;
import org.distributedea.input.batches.machinelearning.zoo.BatchHeteroMethodsMLZoo;
import org.distributedea.input.batches.machinelearning.zoo.BatchHomoMethodsMLZoo;
import org.distributedea.input.batches.machinelearning.zoo.BatchSingleMethodsMLZoo;
import org.distributedea.input.batches.tsp.cities1083.BatchHeteroMethodsTSP1083;
import org.distributedea.input.batches.tsp.cities1083.BatchHomoMethodsTSP1083;
import org.distributedea.input.batches.tsp.cities1083.BatchSingleMethodsTSP1083;
import org.distributedea.input.batches.tsp.cities2036.BatchHeteroMethodsTSP2036;
import org.distributedea.input.batches.tsp.cities2036.BatchHomoMethodsTSP2036;
import org.distributedea.input.batches.tsp.cities2036.BatchSingleMethodsTSP2036;
import org.distributedea.input.batches.tsp.cities662.BatchHeteroMethodsTSP662;
import org.distributedea.input.batches.tsp.cities662.BatchHomoMethodsTSP662;
import org.distributedea.input.batches.tsp.cities662.BatchSingleMethodsTSP662;
import org.distributedea.input.batches.vc.frb10040.BatchHeteroMethodsVCfrb10040;
import org.distributedea.input.batches.vc.frb10040.BatchHomoMethodsVCfrb10040;
import org.distributedea.input.batches.vc.frb10040.BatchSingleMethodsVCfrb10040;
import org.distributedea.input.batches.vc.frb59265.BatchHeteroMethodsVCfrb59265;
import org.distributedea.input.batches.vc.frb59265.BatchHomoMethodsVCfrb59265;
import org.distributedea.input.batches.vc.frb59265.BatchSingleMethodsVCfrb59265;

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

		
		IInputBatch inputBatchSimpleMethodsTSP2036 = new BatchSingleMethodsTSP2036();
		Batch batchSimpleMethodsTSP2036 = inputBatchSimpleMethodsTSP2036.batch();
		
		IInputBatch inputBatchHomoTSP2036 = new BatchHomoMethodsTSP2036();
		Batch batchHomoTSP2036 = inputBatchHomoTSP2036.batch();
		
		IInputBatch inputBatchHeteroTSP2036 = new BatchHeteroMethodsTSP2036(); 
		Batch batchHeteroTSP2036 = inputBatchHeteroTSP2036.batch();

		
		IInputBatch inputBatchSimpleMethodsTSP662 = new BatchSingleMethodsTSP662();
		Batch batchSimpleMethodsTSP662 = inputBatchSimpleMethodsTSP662.batch();
		
		IInputBatch inputBatchHomoTSP662 = new BatchHomoMethodsTSP662();
		Batch batchHomoTSP662 = inputBatchHomoTSP662.batch();
		
		IInputBatch inputBatchHeteroTSP662 = new BatchHeteroMethodsTSP662(); 
		Batch batchHeteroTSP662 = inputBatchHeteroTSP662.batch();
		
		
		
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
		
		
		IInputBatch inputBatchSingleMethodsCOf02 = new BatchSingleMethodsCOf02();
		Batch batchSingleMethodsCOf02 = inputBatchSingleMethodsCOf02.batch();

		IInputBatch inputBatchHomoMethodsCOf02 = new BatchHomoMethodsCOf02();
		Batch batchHomoMethodsCOf02 = inputBatchHomoMethodsCOf02.batch();

		IInputBatch inputBatchHeteroMethodsCOf02 = new BatchHeteroMethodsCOf02();
		Batch batchHeteroMethodsCOf02 = inputBatchHeteroMethodsCOf02.batch();
		
		
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

		
		IInputBatch inputBatchSingleMethodsCOf10 = new BatchSingleMethodsCOf10();
		Batch batchSingleMethodsCOf10 = inputBatchSingleMethodsCOf10.batch();

		IInputBatch inputBatchHomoMethodsCOf10 = new BatchHomoMethodsCOf10();
		Batch batchHomoMethodsCOf10 = inputBatchHomoMethodsCOf10.batch();

		IInputBatch inputBatchHeteroMethodsCOf10 = new BatchHeteroMethodsCOf10();
		Batch batchHeteroMethodsCOf10 = inputBatchHeteroMethodsCOf10.batch();


		IInputBatch inputBatchSingleMethodsCOf14 = new BatchSingleMethodsCOf14();
		Batch batchSingleMethodsCOf14 = inputBatchSingleMethodsCOf14.batch();

		IInputBatch inputBatchHomoMethodsCOf14 = new BatchHomoMethodsCOf14();
		Batch batchHomoMethodsCOf14 = inputBatchHomoMethodsCOf14.batch();

		IInputBatch inputBatchHeteroMethodsCOf14 = new BatchHeteroMethodsCOf14();
		Batch batchHeteroMethodsCOf14 = inputBatchHeteroMethodsCOf14.batch();

		
		IInputBatch inputBatchSingleMethodsCOf17 = new BatchSingleMethodsCOf17();
		Batch batchSingleMethodsCOf17 = inputBatchSingleMethodsCOf17.batch();

		IInputBatch inputBatchHomoMethodsCOf17 = new BatchHomoMethodsCOf17();
		Batch batchHomoMethodsCOf17 = inputBatchHomoMethodsCOf17.batch();

		IInputBatch inputBatchHeteroMethodsCOf17 = new BatchHeteroMethodsCOf17();
		Batch batchHeteroMethodsCOf17 = inputBatchHeteroMethodsCOf17.batch();

		
		IInputBatch inputBatchSingleMethodsMLZoo = new BatchSingleMethodsMLZoo();
		Batch batchSingleMethodsMLZoo = inputBatchSingleMethodsMLZoo.batch();

		IInputBatch inputBatchHomoMethodsMLZoo = new BatchHomoMethodsMLZoo();
		Batch batchHomoMethodsMLZoo = inputBatchHomoMethodsMLZoo.batch();

		IInputBatch inputBatchHeteroMethodsMLZoo = new BatchHeteroMethodsMLZoo();
		Batch batchHeteroMethodsMLZoo = inputBatchHeteroMethodsMLZoo.batch();

		
		IInputBatch inputBatchSingleMethodsMLWilt = new BatchSingleMethodsMLWilt();
		Batch batchSingleMethodsMLWilt = inputBatchSingleMethodsMLWilt.batch();

		IInputBatch inputBatchHomoMethodsMLWilt = new BatchHomoMethodsMLWilt();
		Batch batchHomoMethodsMLWilt = inputBatchHomoMethodsMLWilt.batch();

		IInputBatch inputBatchHeteroMethodsMLWilt = new BatchHeteroMethodsMLWilt();
		Batch batchHeteroMethodsMLWilt = inputBatchHeteroMethodsMLWilt.batch();

		

		IInputBatch inputBatchSingleMethodsVCfrb10040 = new BatchSingleMethodsVCfrb10040();
		Batch batchSingleMethodsVCfrb10040 = inputBatchSingleMethodsVCfrb10040.batch();

		IInputBatch inputBatchHomoMethodsVCfrb10040 = new BatchHomoMethodsVCfrb10040();
		Batch batchHomoMethodsVCfrb10040 = inputBatchHomoMethodsVCfrb10040.batch();

		IInputBatch inputBatchHeteroMethodsVCfrb10040 = new BatchHeteroMethodsVCfrb10040();
		Batch batchHeteroMethodsVCfrb10040 = inputBatchHeteroMethodsVCfrb10040.batch();

		
		IInputBatch inputBatchSingleMethodsVCfrb59265 = new BatchSingleMethodsVCfrb59265();
		Batch batchSingleMethodsVCfrb59265 = inputBatchSingleMethodsVCfrb59265.batch();

		IInputBatch inputBatchHomoMethodsVCfrb59265 = new BatchHomoMethodsVCfrb59265();
		Batch batchHomoMethodsVCfrb59265 = inputBatchHomoMethodsVCfrb59265.batch();
		
		IInputBatch inputBatchHeteroMethodsVCfrb59265 = new BatchHeteroMethodsVCfrb59265();
		Batch batchHeteroMethodsVCfrb59265 = inputBatchHeteroMethodsVCfrb59265.batch();
		
		List<Batch> batchesList = new ArrayList<>();
		
		// TSP
		//batchesList.add(batchTestTSP);
		
		batchesList.add(batchSimpleMethodsTSP1083);
		batchesList.add(batchHomoTSP1083);
		batchesList.add(batchHeteroTSP1083);

		batchesList.add(batchSimpleMethodsTSP2036);
		batchesList.add(batchHomoTSP2036);
		batchesList.add(batchHeteroTSP2036);

		batchesList.add(batchSimpleMethodsTSP662);
		batchesList.add(batchHomoTSP662);
		batchesList.add(batchHeteroTSP662);
		
		// BP
		batchesList.add(batchSingleMethodsBPP1000);
		batchesList.add(batchHomoBPP1000);
		batchesList.add(batchHeteroBPP1000);
		
		// CO
		//batchesList.add(batchSingleMethodsCOf2);
		//batchesList.add(batchHomoCOF2);
		//batchesList.add(batchHeteroCOF2);

		batchesList.add(batchSingleMethodsCOf02);
		batchesList.add(batchHomoMethodsCOf02);
		batchesList.add(batchHeteroMethodsCOf02);
		
		batchesList.add(batchSingleMethodsCOf04);
		batchesList.add(batchHomoMethodsCOf04);
		batchesList.add(batchHeteroMethodsCOf04);
		
		batchesList.add(batchSingleMethodsCOf08);
		batchesList.add(batchHomoMethodsCOf08);
		batchesList.add(batchHeteroMethodsCOf08);

		batchesList.add(batchSingleMethodsCOf10);
		batchesList.add(batchHomoMethodsCOf10);
		batchesList.add(batchHeteroMethodsCOf10);

		batchesList.add(batchSingleMethodsCOf14);
		batchesList.add(batchHomoMethodsCOf14);
		batchesList.add(batchHeteroMethodsCOf14);

		batchesList.add(batchSingleMethodsCOf17);
		batchesList.add(batchHomoMethodsCOf17);
		batchesList.add(batchHeteroMethodsCOf17);
		
		// ML
		//batchesList.add(batchSingleMethodsMLZoo);
		//batchesList.add(batchHomoMethodsMLZoo);
		//batchesList.add(batchHeteroMethodsMLZoo);

		batchesList.add(batchSingleMethodsMLWilt);
		batchesList.add(batchHomoMethodsMLWilt);
		batchesList.add(batchHeteroMethodsMLWilt);
		
		
		// VC		
		batchesList.add(batchSingleMethodsVCfrb10040);
		batchesList.add(batchHomoMethodsVCfrb10040);
		batchesList.add(batchHeteroMethodsVCfrb10040);
				
		batchesList.add(batchSingleMethodsVCfrb59265);
		batchesList.add(batchHomoMethodsVCfrb59265);
		batchesList.add(batchHeteroMethodsVCfrb59265);
		
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
