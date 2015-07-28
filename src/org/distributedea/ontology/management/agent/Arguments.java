package org.distributedea.ontology.management.agent;

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
}
