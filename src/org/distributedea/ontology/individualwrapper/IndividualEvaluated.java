package org.distributedea.ontology.individualwrapper;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.IProblemTool;

/**
 * Ontology which contains one {@link Individual} and fitness value
 * @author stepan
 *
 */
public class IndividualEvaluated implements Concept {

	private static final long serialVersionUID = 1L;

	private Individual individual;
	
	private double fitness;

	@Deprecated
	public IndividualEvaluated() {} // only for Jade
	
	/**
	 * Constructor
	 * @param individual
	 * @param fitness
	 */
	public IndividualEvaluated(Individual individual, double fitness) {
		this.individual = individual;
		this.fitness = fitness;
	}

	/**
	 * Copy constructor
	 * @param individualEval
	 */
	public IndividualEvaluated(IndividualEvaluated individualEval) {
		if (individualEval == null || ! individualEval.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.individual = individualEval.getIndividual().deepClone();
		this.fitness = individualEval.getFitness();
	}
	
	public Individual getIndividual() {
		return individual;
	}
	@Deprecated
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	public double getFitness() {
		return fitness;
	}
	@Deprecated
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public boolean validation(Problem problem, IProblemTool problemTool,
			IAgentLogger logger) {

		if (! individual.valid(logger)) {
			return false;
		}

		double fitnessValue = problemTool
				.fitness(individual, problem, logger);
		
		if (fitness != fitnessValue) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (individual == null || ! individual.valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public IndividualEvaluated deepClone() {
		return new IndividualEvaluated(this);
	}
}
