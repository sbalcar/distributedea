package org.distributedea.ontology.management;

import jade.content.Concept;
import jade.core.AID;

public class CreatedAgent implements Concept {

	private static final long serialVersionUID = 1L;

	private String createdAgentName;
	
	public String getCreatedAgentName() {
		return createdAgentName;
	}
	public void setCreatedAgentName(String createdAgentName) {
		this.createdAgentName = createdAgentName;
	}

	public AID exportCreatedAgentName() {
		return new AID(createdAgentName, false);
	}
	public void importCreatedAgentName(AID createdAgentAID) {
		String globalName = createdAgentAID.getLocalName();
		this.createdAgentName = globalName.substring(0, globalName.indexOf('@'));
	}

}
