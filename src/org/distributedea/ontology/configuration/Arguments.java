package org.distributedea.ontology.configuration;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;


public class Arguments implements Concept {

	private static final long serialVersionUID = 1L;
	
	protected List<Argument> arguments;

	public Arguments() {
		arguments = new ArrayList<Argument>();
	}

	public Arguments(List<Argument> arguments) {
		this.arguments = arguments;
	}
	
	public List<Argument> getArguments() {
		return arguments;
	}
	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

	public int size() {
		return arguments.size();
	}
	
	public static Object[] transformAgrumentsForSniffer(List<Argument> arguments) {
		
		String argumet1 = "";					
		for (Argument argumentI : arguments) {
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
