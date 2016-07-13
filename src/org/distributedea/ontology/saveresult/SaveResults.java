package org.distributedea.ontology.saveresult;

import jade.content.AgentAction;

public class SaveResults implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ResultOfIteration results;

	public SaveResults() {}
	
	public SaveResults(ResultOfIteration results) {
		this.results = results;
	}
	
	public ResultOfIteration getResults() {
		return results;
	}
	public void setResults(ResultOfIteration results) {
		this.results = results;
	}
	
}
