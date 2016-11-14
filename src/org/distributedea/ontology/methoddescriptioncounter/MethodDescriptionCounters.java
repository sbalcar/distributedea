package org.distributedea.ontology.methoddescriptioncounter;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methoddescription.MethodDescription;

/**
 * Ontology represents List of {@link MethodDescriptionCounter}
 * @author stepan
 *
 */
public class MethodDescriptionCounters implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<MethodDescriptionCounter> counters;
	

	/**
	 * Constructor
	 */
	public MethodDescriptionCounters() {
		this.counters = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param countersList
	 */
	public MethodDescriptionCounters(List<MethodDescriptionCounter> countersList) {
		if (countersList == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (MethodDescriptionCounter counterI : countersList) {
			if (counterI == null || ! counterI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						List.class.getSimpleName() + " is not valid");
			}
		}
		
		this.counters = countersList;
	}
	
	/**
	 * Copy constructor
	 * @param countersToClone
	 */
	public MethodDescriptionCounters(MethodDescriptionCounters countersToClone) {
		if (countersToClone == null || ! countersToClone.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptionCounters.class.getSimpleName() + " is not valid");
		}
		
		for (MethodDescriptionCounter counterI : countersToClone.getCounters()) {
			addCounter(counterI.deepClone());
		}
	}
	
	public List<MethodDescriptionCounter> getCounters() {
		return counters;
	}
	@Deprecated
	public void setCounters(List<MethodDescriptionCounter> counters) {
		this.counters = counters;
	}
	
	public void addCounter(MethodDescriptionCounter counter) {
		if (counter == null || ! counter.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptionCounter.class.getSimpleName() + "is not valid");
		}
		
		if (this.counters == null) {
			this.counters = new ArrayList<>();
		}
		this.counters.add(counter);
	}

	public boolean containsMethodDescription(MethodDescription methodDescription) {
		
		if (exportMethodDescriptionCounterOf(methodDescription) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Exports {@link MethodDescriptionCounter} of given {@link MethodDescription}
	 * @param methodDescription
	 * @return
	 */
	public MethodDescriptionCounter exportMethodDescriptionCounterOf(MethodDescription methodDescription) {
		
		for (MethodDescriptionCounter counterI : this.counters) {
			MethodDescription methodDescriptionI =
					counterI.getDescription();
			if (methodDescriptionI.equals(methodDescription)) {
				return counterI;
			}
		}
		return null;
	}
	
	public void incrementCounterOf(MethodDescription methodDescription) {
		
		if (! containsMethodDescription(methodDescription)) {
			addCounter(new MethodDescriptionCounter(methodDescription, 0));
		}
		
		MethodDescriptionCounter counter =
				exportMethodDescriptionCounterOf(methodDescription);
		counter.increment();
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public MethodDescriptionCounters deepClone() {
		
		return new MethodDescriptionCounters(this);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (counters == null) {
			return false;
		}
		for (MethodDescriptionCounter counterI : counters) {
			if (counterI == null || ! counterI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
}
