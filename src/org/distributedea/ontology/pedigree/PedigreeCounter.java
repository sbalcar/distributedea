package org.distributedea.ontology.pedigree;

import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;


/**
 * Ontology represents pedigree of one {@link Individual}.
 * Pedigree does not directly include tree but only counts
 * of methods, which has adjusted the result.
 * @author stepan
 *
 */
public class PedigreeCounter extends Pedigree {

	private static final long serialVersionUID = 1L;
	
	private MethodDescriptionNumbers counters;

	
	/**
	 * Constructor
	 */
	@Deprecated
	public PedigreeCounter() {
		this.counters = new MethodDescriptionNumbers();
	}

	/**
	 * Constructor
	 * @param methodCounters
	 */
	public PedigreeCounter(MethodDescriptionNumbers methodCounters) {
		if (methodCounters == null || ! methodCounters.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedigreeCounter.class.getSimpleName() + " is not valid");			
		}
		this.counters = methodCounters;
	}
	
	PedigreeCounter(PedigreeParameters parameters) {
		this.counters = new MethodDescriptionNumbers();
		this.counters.incrementCounterOf(parameters.methodDescription);
	}

	PedigreeCounter(List<Pedigree> pedigrees,
			PedigreeParameters pedParams) {
		if (pedigrees == null || pedigrees.isEmpty() || pedigrees.size() > 3) {
			throw new IllegalArgumentException("Argument " +
					PedigreeCounter.class.getSimpleName() + " is not valid");
		}
		for (Pedigree pedigreeI : pedigrees) {
			if (pedigreeI == null || ! pedigreeI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						PedigreeCounter.class.getSimpleName() + " is not valid");
			}			
		}

		MethodDescriptionNumbers numbers = new MethodDescriptionNumbers();
		for (Pedigree pedigreeI : pedigrees) {
			PedigreeCounter pedigCounterI = (PedigreeCounter) pedigreeI;
			MethodDescriptionNumbers countersI =
					pedigCounterI.getCounters();
			numbers.addMetDescNumbersAsCounters(countersI.getMethDescNumbers());
		}
		this.counters = numbers;
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

	
	
	public MethodDescriptionNumbers getCounters() {
		return counters;
	}
	@Deprecated
	public void setCounters(MethodDescriptionNumbers counters) {
		if (counters == null || ! counters.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptionNumbers.class.getSimpleName() + " is not valid");			
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

	@Override
	public MethodDescriptionNumbers exportCreditsOfMethodDescriptions() {
		
		return this.getCounters().deepClone();
	}
	
}
