package org.distributedea.ontology.pedigree;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounters;


/**
 * Ontology represents pedigree of one {@link Individual}
 * @author stepan
 *
 */
public class PedigreeCounter extends Pedigree {

	private static final long serialVersionUID = 1L;
	
	private MethodDescriptionCounters counters;

	
	/**
	 * Constructor
	 */
	public PedigreeCounter() {
		this.counters = new MethodDescriptionCounters();
	}
	
	/**
	 * Copy Constructor
	 * @param pedigreeToClone
	 */
	public PedigreeCounter(PedigreeCounter pedigreeToClone) {
		if (pedigreeToClone == null || ! pedigreeToClone.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedigreeCounter.class.getSimpleName() + " is not valid");
		}
		this.counters = pedigreeToClone.getCounters().deepClone();
	}

	
	
	public MethodDescriptionCounters getCounters() {
		return counters;
	}
	@Deprecated
	public void setCounters(MethodDescriptionCounters counters) {
		if (counters == null || ! counters.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptionCounters.class.getSimpleName() + " is not valid");			
		}
		this.counters = counters;
	}

	public void incrementCounterOf(MethodDescription methodDescription) {
		
		counters.incrementCounterOf(methodDescription);
	}
	
	/**
	 * Returns clone
	 */
	public PedigreeCounter deepClone() {
		return new PedigreeCounter(this);
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (counters == null || ! counters.valid(logger)) {
			return false;
		}
		return true;
	}
	
}
