package org.distributedea.ontology.computing.result;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.job.JobID;

import jade.content.Concept;

public class ResultOfComputing implements Concept {

	private static final long serialVersionUID = 1L;
		
	private AgentDescription agentDescription;

	private Individual bestIndividual;
	private double fitnessValue;

	private JobID jobID;
	
	
	public AgentDescription getAgentDescription() {
		return agentDescription;
	}
	public void setAgentDescription(AgentDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	
	public Individual getIndividual() {
		return bestIndividual;
	}
	public void setBestIndividual(Individual bestIndividual) {
		this.bestIndividual = bestIndividual;
	}
	
	public double getFitnessValue() {
		return fitnessValue;
	}
	public void setFitnessValue(double fitnessValue) {
		this.fitnessValue = fitnessValue;
	}
	
	public JobID getJobID() {
		return jobID;
	}
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}
	
	public AgentConfiguration exportAgentConfiguration() {
		return agentDescription.getAgentConfiguration();
	}
	
}
