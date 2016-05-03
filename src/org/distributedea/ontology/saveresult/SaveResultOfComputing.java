package org.distributedea.ontology.saveresult;

import org.distributedea.ontology.computing.result.ResultOfComputing;

import jade.content.AgentAction;

public class SaveResultOfComputing implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ResultOfComputing result;

	public ResultOfComputing getResult() {
		return result;
	}
	public void setResult(ResultOfComputing result) {
		this.result = result;
	}
	
}
