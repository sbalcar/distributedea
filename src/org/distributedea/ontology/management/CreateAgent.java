package org.distributedea.ontology.management;

import org.distributedea.ontology.management.agent.Arguments;

import jade.content.AgentAction;

public class CreateAgent implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private String type;
	private String name;
	private Arguments arguments;
	
	public Arguments getArguments() {
		return arguments;
	}
	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
