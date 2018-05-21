package org.distributedea.ontology.problem.matrixfactorization.latentfactor;

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
	
	
	/**
	 * Returns equals
	 * @param other
	 * @return
	 */
	public boolean equals(Object other);
	
}
