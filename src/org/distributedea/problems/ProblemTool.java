package org.distributedea.problems;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.exceptions.ProblemToolException;

public interface ProblemTool {

	public Class<?> problemWhichSolves();
	public Class<?> reprezentationWhichUses();
	
	public void initialization(Problem problem, AgentLogger logger) throws ProblemToolException;
	public void exit() throws ProblemToolException;
	
	/**
	 * Reads instance of the Problem from the file,
	 * for the illegal input file name returns null
	 * @param inputFileName
	 * @param logger
	 * @return
	 */
	public Problem readProblem(String inputFileName, AgentLogger logger);
	
	/**
	 * Reads instance of the Solution(Individual) from the file,
	 * for the illegal input file name returns null
	 * @param fileName
	 * @param problem
	 * @param logger
	 * @return
	 */
	public Individual readSolution(String fileName, Problem problem,
			AgentLogger logger);
	
	public Individual generateIndividual(Problem problem, AgentLogger logger);
	public Individual generateFirstIndividual(Problem problem, AgentLogger logger);
	public Individual generateNextIndividual(Problem problem,
			Individual individual, AgentLogger logger);
	
	public double fitness(Individual individual, Problem problem,
			AgentLogger logger);
	public Individual improveIndividual(Individual individual, Problem problem,
			AgentLogger logger) throws ProblemToolException;
    
	public Individual getNeighbor(Individual individual, Problem problem,
			long neighborIndex, AgentLogger logger) throws ProblemToolException;
	
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Problem problem, AgentLogger logger)
			throws ProblemToolException;
	public Individual[] createNewIndividual(Individual individual1, 
			Individual individual2, Individual individual3,
			Individual individual4, Problem problem, AgentLogger logger)
			throws ProblemToolException;
	
}
