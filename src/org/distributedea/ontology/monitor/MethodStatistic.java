package org.distributedea.ontology.monitor;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;

/**
 * Ontology represents statistic of one method computation.
 * @author stepan
 *
 */
public class MethodStatistic implements Concept {

	private static final long serialVersionUID = 1L;

	private AgentDescription agentDescription;
	
	private MethodStatisticResult methodStatisticResult;
	
	@Deprecated // only for Jade
	public MethodStatistic() {}
	
	/**
	 * Constructor
	 * @param agentDescription
	 * @param methodStatisticResult
	 */
	public MethodStatistic(AgentDescription agentDescription,
			MethodStatisticResult methodStatisticResult) {
		if (agentDescription == null || ! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		if (methodStatisticResult == null || ! methodStatisticResult.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		this.agentDescription = agentDescription;
		this.methodStatisticResult = methodStatisticResult;
	}
	
	public AgentDescription getAgentDescription() {
		return agentDescription;
	}
	@Deprecated
	public void setAgentDescription(AgentDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	public MethodStatisticResult getMethodStatisticResult() {
		return methodStatisticResult;
	}
	@Deprecated
	public void setMethodStatisticResult(MethodStatisticResult methodStatisticResult) {
		this.methodStatisticResult = methodStatisticResult;
	}
	
	public IndividualEvaluated exportBestIndividual() {
		
		return methodStatisticResult.getBestIndividual();
	}
	public AgentDescription exportAgentDescriptionClone() {
		
		return getAgentDescription().deepClone();
	}
	public InputAgentDescription exportInputAgentDescriptionClone() {
		
		return exportAgentDescriptionClone().exportInputAgentDescription();
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (agentDescription == null || ! agentDescription.valid(logger)) {
			return false;
		}
		if (methodStatisticResult == null || ! methodStatisticResult.valid(logger)) {
			return false;
		}
		return true;
	}
}
