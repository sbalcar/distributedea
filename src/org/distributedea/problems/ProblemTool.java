package org.distributedea.problems;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;

public interface ProblemTool {

	public Problem readProblem(String inputFileName, AgentLogger logger);
	
	public Individual generateIndividual(Problem problem, AgentLogger logger);
	
	public double fitness(Individual individual, Problem problem,
			AgentLogger logger);
	public Individual improveIndividual(Individual individual, Problem problem,
			AgentLogger logger);
    	
	public Individual createNewIndividual(Individual individual1,
			Individual individual2, AgentLogger logger);
	public Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			Individual individual4, AgentLogger logger);
	
}
