package org.distributedea.input.batches;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;

/**
 * Interface for structure for creating set of input {@link Batch} 
 * @author stepan
 *
 */
public interface IInputBatch {

	public Batch batch();
}
