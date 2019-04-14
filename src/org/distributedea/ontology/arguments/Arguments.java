package org.distributedea.ontology.arguments;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology represents structure of {@link Argument} list
 * @author stepan
 *
 */
public class Arguments implements Concept {

	private static final long serialVersionUID = 1L;
	
	protected List<Argument> arguments;

	/**
	 * Constructor
	 */
	public Arguments() {
		this.arguments = new ArrayList<Argument>();
	}

	/**
	 * Constructor
	 * @param name
	 * @param value
	 */
	public Arguments(String name, String value) {
		this(Arrays.asList(new Argument(name, value)));
	}
	
	/**
	 * Constructor
	 * @param argument
	 */
	public Arguments(Argument argument) {
		this(Arrays.asList(argument));
	}
	
	/**
	 * Constructor
	 * @param argument0
	 * @param argument1
	 */
	public Arguments(Argument argument0, Argument argument1) {
		this(Arrays.asList(argument0, argument1));
	}

	/**
	 * Constructor
	 * @param argument0
	 * @param argument1
	 * @param argument2
	 */
	public Arguments(Argument argument0, Argument argument1, Argument argument2) {
		this(Arrays.asList(argument0, argument1, argument2));
	}

	/**
	 * Constructor
	 * @param argument0
	 * @param argument1
	 * @param argument2
	 * @param argument3
	 */
	public Arguments(Argument argument0, Argument argument1, Argument argument2,
			Argument argument3) {
		this(Arrays.asList(argument0, argument1, argument2, argument3));
	}
	
	/**
	 * Constructor
	 * @param arguments
	 */
	public Arguments(List<Argument> arguments) {
		if (arguments == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		TrashLogger logger = new TrashLogger();
		for (Argument argumentI : arguments) {
			if (argumentI == null || ! argumentI.valid(logger)) {
				throw new IllegalArgumentException("Argument " +
						List.class.getSimpleName() + " is not valid");
			}
		}
		this.arguments = arguments;
	}
	
	/**
	 * Copy Constructor
	 * @param arguments
	 */
	public Arguments(Arguments arguments) {
		if (arguments == null ||
				! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
		this.arguments = new ArrayList<Argument>();
		for (Argument argumentI : arguments.getArguments()) {
			addArgument(argumentI.deepClone());
		}
	}
	
	/**
	 * Returns list of {@link Argument}
	 * @return
	 */
	public List<Argument> getArguments() {
		return arguments;
	}
	@Deprecated
	public void setArguments(List<Argument> arguments) {
		if (! new Arguments(arguments).valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		this.arguments = arguments;
	}

	/**
	 * Add {@link Argument}
	 * @param argument
	 */
	public void addArgument(Argument argument) {
		if (argument == null ||
				! argument.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Argument.class.getSimpleName() + " is not valid");
		}
		this.arguments.add(argument);
	}

	/**
	 * Replace {@link Argument} with the new value
	 * @param argumentNew
	 */
	public void replaceArgument(Argument argumentNew) {
		if (argumentNew == null || ! argumentNew.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Argument.class.getSimpleName() + " is not valid");
		}
		
		Argument arg = exportArgument(argumentNew.getName());
		int index = getArguments().indexOf(arg);
		
		getArguments().set(index, argumentNew);
	}
		
	public int size() {
		return arguments.size();
	}
	
	/**
	 * Export {@link Argument} by name
	 * @param argumentName
	 * @return
	 */
	public Argument exportArgument(String argumentName) {
		
		for (Argument argumentI : this.arguments) {
			if (argumentI.getName().equals(argumentName)) {
				return argumentI;
			}
		}
		return null;
	}
	
	/**
	 * Exports random selected {@link Argument}
	 * @return
	 */
	public Argument exportRandomArgument() {
		
		Random rnd = new Random();
		int index = rnd.nextInt(size());
		
		return getArguments().get(index);
	}
	
	/**
	 * Exports {@link Arguments} as string
	 * @return
	 */
	public String exportToString() {
		
		if (this.arguments.isEmpty()) {
			return "";
		}
		
		String argumentsString = "";
		
		for (Argument argumentI : this.arguments) {
			argumentsString += argumentI.getName() + "=" + argumentI.getValue() + ", ";
		}
		return argumentsString.substring(0, argumentsString.length() -2);
	}
	
	/**
	 * Test validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (arguments == null) {
			return false;
		}
		for (Argument argumentI : arguments) {
			if (argumentI == null || ! argumentI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public Arguments deepClone() {
		return new Arguments(this);
	}

	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof Arguments)) {
	        return false;
	    }
	    
	    Arguments arOuther = (Arguments)other;
	    if (! arOuther.valid(new TrashLogger())) {
	    	return false;
	    }
	    
	    if (getArguments().size() != arOuther.getArguments().size()) {
	    	return false;
	    }
    	for (int argumentIndex = 0;
    			argumentIndex < getArguments().size(); argumentIndex++) {
    		
    		Argument argThis = getArguments().get(argumentIndex);
    		Argument argOther = arOuther.getArguments().get(argumentIndex);
    		
    		if (! argThis.equals(argOther)) {
    			return false;
    		}
    	}
	    return true;
	}
	

    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		String value = "";
		for (Argument argumentI : arguments) {
			value += argumentI.toString() + "-";
		}
				
		return value;
	}
	
	public String toLogString() {
		String value = "";
		for (Argument argumentI : arguments) {
			value += argumentI.toLogString() + ", ";
		}
		
		if (value.endsWith(", ")) {
			value = value.substring(0, value.length() -2);
		}
		
		return value;
	}

	
	/**
	 * Export arguments for Jade
	 * @return
	 */
	public Object[] exportAgrumentsForJade() {
		
		Object[] jadeArgs = new Object[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			
			Argument argumentI = arguments.get(i);
			
			jadeArgs[i] = argumentI.exportXML();
		}
		
		return jadeArgs;
	}
	
	public static Arguments importArguments(Object[] jadeArgs) {
		
		Arguments args = new Arguments();
		
		for (int i = 0; i < jadeArgs.length; i++) {
			
			Object oI = jadeArgs[i];
			Argument argI = Argument.importXML((String) oI);
			
			args.addArgument(argI);
		}
		
		return args;
	}
	
	public static Object[] transformAgrumentsForSniffer(Arguments arguments) {
		
		String argumet1 = "";					
		for (Argument argumentI : arguments.getArguments()) {
			String agentNameI = argumentI.getValue();
					//Configuration.CONTAINER_NUMBER_PREFIX + numberOfContainer;
			argumet1 += agentNameI + "; ";
		}
		argumet1.trim();
		
		Object[] args = null;
		
		//removes the last semicolon
		if (! argumet1.isEmpty()) {
			args = new Object[1];
			args[0] = argumet1.substring(0, argumet1.length() -2);
		}
		
		return args;
	}
	
}
