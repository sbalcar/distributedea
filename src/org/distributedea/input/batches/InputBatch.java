package org.distributedea.input.batches;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;

/**
 * Structure for creating set of input {@link Batch} 
 * @author stepan
 *
 */
public abstract class InputBatch {

	public abstract Batch batch();
}
