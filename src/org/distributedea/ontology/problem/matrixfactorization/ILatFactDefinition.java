package org.distributedea.ontology.problem.matrixfactorization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;

import jade.content.Concept;

/**
 * Ontology interface for definition of the {@link LatentFactor} length
 * @author stepan
 *
 */
public interface ILatFactDefinition extends Concept {

	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger);
	
	/**
	 * Returns clone
	 * @return
	 */
	public ILatFactDefinition deepClone();
	
	
	public boolean equals(Object other);
	
}
