package org.distributedea.ontology.problem.matrixfactorization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;

/**
 * Ontology for definition of the {@link LatentFactor} length,
 * takes given ratings as non-continuous set of IDs
 * @author stepan
 *
 */
public class LatFactSet implements ILatFactDefinition {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public LatFactSet() {}
	
	/**
	 * Copy constructor
	 * @param problem
	 */
	public LatFactSet(LatFactSet latFactRange) {
		if (latFactRange == null || ! latFactRange.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatFactSet.class.getSimpleName() + " is not valid");			
		}
	}
	
	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public ILatFactDefinition deepClone() {
		return new LatFactSet(this);
	}

}
