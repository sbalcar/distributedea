package org.distributedea.ontology.argumentsdefinition;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;

/**
 * Ontology for representation a set of {@link ArgumentDef}s. Allow
 * to restrict values of ontology {@link Arguments}.
 * @author stepan
 *
 */
public class ArgumentsDef implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<ArgumentDef> argumentsDef;
	
	/**
	 * Constructor
	 */
	public ArgumentsDef() {
		this.argumentsDef = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param argumentsDef
	 */
	public ArgumentsDef(List<ArgumentDef> argumentsDef) {
		setArgumentsDef(argumentsDef);
	}

	/**
	 * Copy constructor
	 * @param argumentsDef
	 */
	public ArgumentsDef(ArgumentsDef argumentsDef) {
		if (argumentsDef == null ||
				! argumentsDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ArgumentsDef.class.getSimpleName() + " is not valid");
		}
	
		List<ArgumentDef> argumentsDefCopy = new ArrayList<>();
		for (ArgumentDef argumentDefI : argumentsDef.getArgumentsDef()) {
			argumentsDefCopy.add(argumentDefI.deepClone());
		}
		this.argumentsDef = argumentsDefCopy;
	}
	
	
	public List<ArgumentDef> getArgumentsDef() {
		return this.argumentsDef;
	}
	public void setArgumentsDef(List<ArgumentDef> argumentsDef) {
		if (argumentsDef == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.argumentsDef = argumentsDef;
	}
	
	public void addArgumentsDef(ArgumentDef argumentDef) {
		if (argumentDef == null || ! argumentDef.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.argumentsDef.add(argumentDef);
	}

	public int size() {
		if (this.argumentsDef == null) {
			return 0;
		}
		return this.argumentsDef.size();
	}

	
	/**
	 * Exports {@link ArgumentsDef} by name
	 * @param name
	 * @return
	 */
	public ArgumentDef exportArgumentsDef(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Argument " +
					String.class.getSimpleName() + " is not valid");
		}
		
		if (this.argumentsDef == null) {
			return null;
		}
		for (ArgumentDef argI : this.argumentsDef) {
			if (name.equals(argI.getName())) {
				return argI;
			}
		}
		
		return null;
	}

	/**
	 * Exports random selected {@link ArgumentDef}
	 * @return
	 */
	public ArgumentDef exportRandomArgumentDef() {
		if (size() == 0) {
			return null;
		}
		
		Random rnd = new Random();
		int index = rnd.nextInt(size());
		
		return getArgumentsDef().get(index);
	}
	
	/**
	 * Exports random generated values of {@link Argument}
	 * @return
	 */
	public Arguments exportRandomGeneratedArguments() {

		Arguments arguments = new Arguments();
		
		for (ArgumentDef argI : getArgumentsDef()) {
			arguments.addArgument(
					argI.exportRandomValue());
		}
		
		return arguments;
	}

	public Arguments exportMinArumentValues() {

		Arguments arguments = new Arguments();
		
		for (ArgumentDef argI : getArgumentsDef()) {
			arguments.addArgument(
					argI.exportMinValue());
		}
		
		return arguments;
	}
	
	/**
	 * Exports a set of {@link ArgumentDef} which allow a change of value
	 * @return
	 */
	public ArgumentsDef exportsArgumentsWithoutRestrictionForFixedValue() {

		List<ArgumentDef> argumentsWithoutRst = new ArrayList<>();
		
		for (ArgumentDef argI : getArgumentsDef()) {
			
			if (! argI.representsRestrictionForFixedValue()) {
				argumentsWithoutRst.add(argI);
			}
		}

		return new ArgumentsDef(argumentsWithoutRst);
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	/**
	 * Returns clone
	 * @return
	 */
	public ArgumentsDef deepClone() {
		return new ArgumentsDef(this);
	}
}
