package org.distributedea.ontology.saveresult;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import jade.content.Concept;

import org.distributedea.agents.systemagents.centralmanager.planner.history.MethodInstanceDescription;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.monitor.MethodStatisticResult;

public class ResultOfMethodInstanceIteration implements Concept {

	private static final long serialVersionUID = 1L;

	private MethodInstanceDescription methodInstanceDescription;
	
	private AgentDescription agentDescription;
	
	private MethodStatisticResult methodStatisticResult;

	
	
	public MethodInstanceDescription getMethodInstanceDescription() {
		return methodInstanceDescription;
	}
	public void setMethodInstanceDescription(
			MethodInstanceDescription methodInstanceDescription) {
		this.methodInstanceDescription = methodInstanceDescription;
	}

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

	public void exportXML(File dir) throws FileNotFoundException, JAXBException {
		
		if (dir == null || (! dir.exists()) || (! dir.isDirectory())) {
			return;
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
