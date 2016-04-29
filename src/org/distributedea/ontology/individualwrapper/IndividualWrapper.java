package org.distributedea.ontology.individualwrapper;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individuals.Individual;

import jade.content.Concept;

/**
 * Ontology for solution + description representation
 */
public class IndividualWrapper implements Concept {

	private static final long serialVersionUID = -3883073526379640439L;

	private Individual individual;
	
	private AgentDescription agentDescription;

	private String jobID;
	
	
	public Individual getIndividual() {
		return individual;
	}
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	public AgentDescription getAgentDescription() {
		return agentDescription;
	}
	public void setAgentDescription(AgentDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	
}
