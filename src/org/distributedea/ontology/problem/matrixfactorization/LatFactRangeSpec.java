package org.distributedea.ontology.problem.matrixfactorization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;

/**
 * Ontology for definition of the {@link LatentFactor} length,
 * takes given ratings as continuous interval between min and max of given IDs
 * @author stepan
 *
 */
public class LatFactRangeSpec implements ILatFactDefinition {

	private static final long serialVersionUID = 1L;

	private int min;
	private int max;
	
	/**
	 * Constructor
	 */
	public LatFactRangeSpec() { // Only for Jade
	}
	
	/**
	 * Constructor
	 * @param min
	 * @param max
	 */
	public LatFactRangeSpec(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		setMin(min);
		setMax(max);
	}
	
	/**
	 * Copy constructor
	 * @param problem
	 */
	public LatFactRangeSpec(LatFactRangeSpec latFactRangeSpec) {
		if (latFactRangeSpec == null || ! latFactRangeSpec.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					LatFactRangeSpec.class.getSimpleName() + " is not valid");			
		}
		this.setMin(latFactRangeSpec.getMin());
		this.setMax(latFactRangeSpec.getMax());
	}
	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		return min < max;
	}

	@Override
	public ILatFactDefinition deepClone() {
		return new LatFactRangeSpec(this);
	}
	
}
