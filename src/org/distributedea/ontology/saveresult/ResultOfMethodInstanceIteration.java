package org.distributedea.ontology.saveresult;

import java.io.File;
import java.io.IOException;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
import org.distributedea.ontology.monitor.MethodStatisticResult;

/**
 * Ontology represent one result of method instance
 * @author stepan
 *
 */
public class ResultOfMethodInstanceIteration implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodInstanceDescription methodInstanceDescription;
	
	private AgentDescription agentDescription;
	
	private MethodStatisticResult methodStatisticResult;

	
	@Deprecated
	public ResultOfMethodInstanceIteration() {}
	
	/**
	 * Constructor
	 * @param methodInstanceDescription
	 * @param agentDescription
	 * @param methodStatisticResult
	 */
	public ResultOfMethodInstanceIteration(
			MethodInstanceDescription methodInstanceDescription,
			AgentDescription agentDescription,
			MethodStatisticResult methodStatisticResult) {
		IAgentLogger logger = new TrashLogger();
		
		if (methodInstanceDescription == null || ! methodInstanceDescription.valid(logger)) {
			throw new IllegalArgumentException();
		}
		if (agentDescription == null || ! agentDescription.valid(logger)) {
			throw new IllegalArgumentException();
		}
		if (methodStatisticResult == null || ! methodStatisticResult.valid(logger)) {
			throw new IllegalArgumentException();
		}
		this.methodInstanceDescription = methodInstanceDescription;
		this.agentDescription = agentDescription;
		this.methodStatisticResult = methodStatisticResult;
	}
	
	
	public MethodInstanceDescription getMethodInstanceDescription() {
		return methodInstanceDescription;
	}
	@Deprecated
	public void setMethodInstanceDescription(
			MethodInstanceDescription methodInstanceDescription) {
		this.methodInstanceDescription = methodInstanceDescription;
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

	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (methodInstanceDescription == null || ! methodInstanceDescription.valid(logger)) {
			return false;
		}
		if (agentDescription == null || ! agentDescription.valid(logger)) {
			return false;
		}
		if (methodStatisticResult == null || ! methodStatisticResult.valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Exports to XML
	 * @param dir
	 * @throws IOException
	 */
	public void exportXML(File dir) throws IOException {
		
		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		if (methodInstanceDescription != null) {
			methodInstanceDescription.exportXML(dir.getParentFile());
		}
		
		if (agentDescription != null) {
			agentDescription.exportXML(dir);
		}
		
		if (methodStatisticResult != null) {
			methodStatisticResult.exportXML(dir);
		}
	}
	
}
