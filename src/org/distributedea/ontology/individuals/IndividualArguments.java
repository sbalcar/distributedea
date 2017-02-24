package org.distributedea.ontology.individuals;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;

/**
 * Ontology represents one {@link Arguments} based {@link Individual}
 * @author stepan
 *
 */
public class IndividualArguments extends Individual {

	private static final long serialVersionUID = 1L;

	private Arguments arguments;
	
	/**
	 * Constructor
	 */
	public IndividualArguments() { // only for Jade
		this.arguments = new Arguments();
	}
	
	/**
	 * Constructor
	 * @param arguments
	 */
	public IndividualArguments(Arguments arguments) {
		setArguments(arguments);
	}
	
	/**
	 * Copy Constructor
	 * @param indivArguments
	 */
	public IndividualArguments(IndividualArguments indivArguments) {
		if (indivArguments == null || ! indivArguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualArguments.class.getSimpleName() + " is not valid");
		}

		arguments = indivArguments.getArguments();
	}

	
	public Arguments getArguments() {
		return arguments;
	}
	public void setArguments(Arguments arguments) {
		if (arguments == null || ! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}

		this.arguments = arguments;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		
		if (arguments == null) {
			return false;
		}
		
		return arguments.valid(logger);
	}

	@Override
	public Individual deepClone() {
		return new IndividualArguments(this);
	}

	@Override
	public String toLogString() {
		return arguments.toString();
	}

}
