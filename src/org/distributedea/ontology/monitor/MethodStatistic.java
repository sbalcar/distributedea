package org.distributedea.ontology.monitor;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individualhash.IndividualHash;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;

/**
 * Ontology represents statistic of one computational method
 * @author stepan
 *
 */
public class MethodStatistic implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodDescription agentDescription;
	
	private MethodStatisticResult methodStatisticResult;
	
	@Deprecated // only for Jade
	public MethodStatistic() {}
	
	/**
	 * Constructor
	 * @param agentDescription
	 * @param methodStatisticResult
	 */
	public MethodStatistic(MethodDescription agentDescription,
			MethodStatisticResult methodStatisticResult) {
		if (agentDescription == null || ! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		if (methodStatisticResult == null || ! methodStatisticResult.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodStatisticResult.class.getSimpleName() + " is not valid");
		}
		
		this.agentDescription = agentDescription;
		this.methodStatisticResult = methodStatisticResult;
	}
	
	public MethodDescription getMethodDescription() {
		return agentDescription;
	}
	@Deprecated
	public void setMethodDescription(MethodDescription methodDescription) {
		this.agentDescription = methodDescription;
	}
	
	public MethodStatisticResult getMethodStatisticResult() {
		return methodStatisticResult;
	}
	@Deprecated
	public void setMethodStatisticResult(MethodStatisticResult methodStatisticResult) {
		this.methodStatisticResult = methodStatisticResult;
	}
	
	public IndividualHash exportBestIndividual() {
		
		return methodStatisticResult.getBestIndividual();
	}
	public MethodDescription exportMethodDescriptionClone() {
		
		return getMethodDescription().deepClone();
	}
	public InputMethodDescription exportInputMethodDescriptionClone() {
		
		return exportMethodDescriptionClone().exportInputMethodDescription();
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
