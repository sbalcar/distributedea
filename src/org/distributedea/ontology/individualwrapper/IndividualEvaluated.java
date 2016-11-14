package org.distributedea.ontology.individualwrapper;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.pedigree.Pedigree;
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

	private Pedigree pedigree;
	
	
	@Deprecated
	public IndividualEvaluated() {} // only for Jade
	
	/**
	 * Constructor
	 * @param individual
	 * @param fitness
	 */
	public IndividualEvaluated(Individual individual, double fitness, Pedigree pedigree) {
		this.individual = individual;
		this.fitness = fitness;
		this.pedigree = pedigree;
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
		
		if (individualEval.getPedigree() != null) {
			this.pedigree = individualEval.getPedigree().deepClone();
		}
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

	public Pedigree getPedigree() {
		return pedigree;
	}
	public void setPedigree(Pedigree pedigree) {
		this.pedigree = pedigree;
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
	
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof IndividualEvaluated)) {
	        return false;
	    }
	    
	    IndividualEvaluated ieOuther = (IndividualEvaluated)other;
	    
	    boolean areIndividualsEqual =
	    		getIndividual().equals(ieOuther.getIndividual());

	    boolean areFitnessEqual =
	    		getFitness() == ieOuther.getFitness();
	    
	    return areIndividualsEqual && areFitnessEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return "" + getIndividual().toString() +
				getFitness();
	}
	
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (individual == null || ! individual.valid(logger)) {
			return false;
		}
		if (pedigree != null && ! pedigree.valid(logger)) {
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
