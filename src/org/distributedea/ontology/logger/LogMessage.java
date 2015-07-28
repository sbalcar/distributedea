package org.distributedea.ontology.logger;

import jade.content.Concept;

public class LogMessage implements Concept {

	private static final long serialVersionUID = 1L;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
