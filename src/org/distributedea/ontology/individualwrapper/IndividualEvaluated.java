package org.distributedea.ontology.individualwrapper;

import jade.content.Concept;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;

public class IndividualEvaluated implements Concept {

	private static final long serialVersionUID = 1L;

	private Individual individual;
	
	private double fitness;

	public Individual getIndividual() {
		return individual;
	}
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public boolean validation(Problem problem, ProblemTool problemTool,
			AgentLogger logger) {

		if (! individual.validation()) {
			return false;
		}

		double fitnessValue = problemTool
				.fitness(individual, problem, logger);
		
		if (fitness != fitnessValue) {
			return false;
		}
		
		return true;
	}
}
