package org.distributedea.ontology.results;

import jade.content.AgentAction;

public class PartResult  implements AgentAction  {

	private static final long serialVersionUID = 1L;
	
	private String agentDescription;
	private long generationNumber;
	private double fitnessResult;
	
	public String getAgentDescription() {
		return agentDescription;
	}
	public void setAgentDescription(String agentDescription) {
		this.agentDescription = agentDescription;
	}
	
	public long getGenerationNumber() {
		return generationNumber;
	}
	public void setGenerationNumber(long generationNumber) {
		this.generationNumber = generationNumber;
	}
	
	public double getFitnessResult() {
		return fitnessResult;
	}
	public void setFitnessResult(double fitnessResult) {
		this.fitnessResult = fitnessResult;
	}
}
