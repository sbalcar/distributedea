package org.distributedea.ontology.computing.result;

import org.distributedea.ontology.individuals.Individual;

import jade.content.Concept;

public class ResultOfComputing implements Concept {

	private static final long serialVersionUID = 1L;

	private String problemToolClass;
	
	private Individual bestIndividual;
	private double fitnessValue;
	
	
	public String getProblemToolClass() {
		return problemToolClass;
	}
	
	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}
	
	public Individual getBestIndividual() {
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
	
}
