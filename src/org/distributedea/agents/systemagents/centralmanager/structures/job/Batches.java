package org.distributedea.agents.systemagents.centralmanager.structures.job;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

public class Batches {

	private List<Batch> batches;
	
	/**
	 * Constructor
	 */
	public Batches() {
		this.batches = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param batches
	 */
	public Batches(List<Batch> batches) {
		if (batches == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " can not be null");			
		}
		this.batches = new ArrayList<>();
		
		for (Batch batchI : batches) {
			addBatch(batchI);
		}
	}
	
	/**
	 * Constructor
	 * @param batches
	 */
	public Batches(Batch batch) {
		if (batch == null || ! batch.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Batch.class.getSimpleName() + " is not valid");			
		}
		this.batches = new ArrayList<>();
		addBatch(batch);
	}	
	
	/**
	 * Add {@link Batch}
	 * @param batch
	 */
	public void addBatch(Batch batch) {
		if (batch == null || ! batch.valid(new TrashLogger())) {
			batch.valid(new TrashLogger());
			throw new IllegalArgumentException("Argument " +
					Batch.class.getSimpleName() + " is not valid");
		}
		
		this.batches.add(batch);
	}
	
	public List<Batch> getBatches() {
		return this.batches;
	}
	
	public boolean containsJobOrPostProc() {
		
		for (Batch batchI: batches) {
			if (! batchI.getJobs().isEmpty() ||
					! batchI.getPostProcessings().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public Batch exportBatch(String batchID) {
		
		for (Batch batchI: batches) {
			if (batchI.getBatchID().equals(batchID)) {
				return batchI;
			}
		}
		
		return null;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (this.batches == null) {
			return false;
		}
		for (Batch batchI : batches) {
			if(! batchI.valid(logger)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Imports {@link Batches} from given directory
	 * @param batchesDir
	 * @return
	 * @throws IOException
	 */
	public static Batches importXML(File batchesDir) throws Exception {
		
		List<Batch> batches = new ArrayList<>();
	
		for (File batchFileI : batchesDir.listFiles()) {
			if (batchFileI.isDirectory()) {
				Batch batchI = Batch.importXML(batchFileI);
				batches.add(batchI);
			}
		}
		
		return new Batches(batches);
	}
	
	/**
	 * Exports {@link Batches} to given directory
	 * @param batchesDir
	 * @throws IOException
	 */
	public void exportXML(File batchesDir) throws Exception {
		if (batchesDir == null || ! batchesDir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}

		for (Batch batchI : this.batches) {
			File batchDirI = new File(batchesDir.getAbsolutePath() +
					File.separator + batchI.getBatchID());
			
			if (batchDirI.exists()) {
				throw new IllegalStateException();
			}
			batchDirI.mkdir();
			batchI.exportXML(batchDirI);
		}
	}
}
