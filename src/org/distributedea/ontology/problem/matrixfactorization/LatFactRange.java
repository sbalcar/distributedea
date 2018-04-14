package org.distributedea.ontology.problem.matrixfactorization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;

/**
 * Ontology for definition of the {@link LatentFactor} length,
 * takes given ratings as continuous interval between min and max of IDs in the dataset
 * @author stepan
 *
 */
public class LatFactRange implements ILatFactDefinition {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public LatFactRange() {}
	
	/**
	 * Copy constructor
	 * @param problem
	 */
	public LatFactRange(LatFactRange latFactRange) {
		if (latFactRange == null || ! latFactRange.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatFactRange.class.getSimpleName() + " is not valid");			
		}
	}
	
	
	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public ILatFactDefinition deepClone() {
		return new LatFactRange(this);
	}

}
