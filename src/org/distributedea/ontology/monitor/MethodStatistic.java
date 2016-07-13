package org.distributedea.ontology.monitor;

import jade.content.Concept;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;

public class MethodStatistic implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentDescription agentDescription;
	
	private MethodStatisticResult methodStatisticResult;
	
	
	public AgentDescription getAgentDescription() {
		return agentDescription;
	}
	public void setAgentDescription(AgentDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	public MethodStatisticResult getMethodStatisticResult() {
		return methodStatisticResult;
	}
	public void setMethodStatisticResult(MethodStatisticResult methodStatisticResult) {
		this.methodStatisticResult = methodStatisticResult;
	}
	
	public IndividualEvaluated exportBestIndividual() {
		
		return methodStatisticResult.getBestIndividual();
	}
}
