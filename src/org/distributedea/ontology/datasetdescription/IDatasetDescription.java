package org.distributedea.ontology.datasetdescription;

import org.distributedea.logging.IAgentLogger;

import jade.content.Concept;

/**
 * Ontological interface which represents description of {@link IDataset}
 * @author stepan
 */
public interface IDatasetDescription extends Concept {

	public String toLogString();
	
	public IDatasetDescription deepClone();
	public boolean valid(IAgentLogger logger);
	
}
