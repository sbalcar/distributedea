package org.distributedea.problems;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.Problem;

/**
 * Interface for {@link ProblemTool} implemntation
 * @author stepan
 *
 */
public interface IProblemTool {

	/**
	 * Returns Class of {@link Problem} which this {@link ProblemTool} serves
	 * @return
	 */
	public Class<?> problemWhichSolves();
	
	/**
	 * Returns Class of {@link Individual} for which provides tool operations
	 * @return
	 */
	public Class<?> reprezentationWhichUses();
	
	/**
	 * Initialization of {@link ProblemTool}
	 * @param problem
	 * @param agentConf
	 * @param logger
	 * @throws ProblemToolException
	 */
	public void initialization(Problem problem, AgentConfiguration agentConf,
			IAgentLogger logger) throws ProblemToolException;
	
	/**
	 * Exit of {@link ProblemTool}
	 * @throws ProblemToolException
	 */
	public void exit() throws ProblemToolException;
	
	/**
	 * Reads instance of the Problem from the file,
	 * for the illegal input file name returns null
	 * @param problemFile
	 * @param logger
	 * @return
	 */
	public Problem readProblem(File fileOfProblem, IAgentLogger logger);
	
	/**
	 * Reads instance of the Solution(Individual) from the file,
	 * for the illegal input file name returns null
	 * @param fileName
	 * @param problem
	 * @param logger
	 * @return
	 */
	public Individual readSolution(File fileOfSolution, Problem problem,
			IAgentLogger logger);
	
	/**
	 * Generated random {@link Individual} as solution for given {@link Problem}
	 * @param problem
	 * @param logger
	 * @return
	 */
	public Individual generateIndividual(Problem problem, IAgentLogger logger);
	public Individual generateFirstIndividual(Problem problem, IAgentLogger logger);
	public Individual generateNextIndividual(Problem problem,
			Individual individual, IAgentLogger logger);
	public IndividualEvaluated generateIndividualEval(Problem problem, IAgentLogger logger);
	public IndividualEvaluated generateFirstIndividualEval(Problem problem, IAgentLogger logger);
	public IndividualEvaluated generateNextIndividualEval(Problem problem,
			Individual individual, IAgentLogger logger);
	
	/**
	 * Evaluates given {@link Individual} as solution of given {@link Problem}
	 * @param individual
	 * @param problem
	 * @param logger
	 * @return
	 */
	public double fitness(Individual individual, Problem problem,
			IAgentLogger logger);
	
	/**
	 * Tries to improve given {@link Individual}. Improving fitness is not guaranteed.
	 * @param individual
	 * @param problem
	 * @param logger
	 * @return
	 * @throws ProblemToolException
	 */
	public Individual improveIndividual(Individual individual, Problem problem,
			IAgentLogger logger) throws ProblemToolException;    
	public IndividualEvaluated improveIndividualEval(IndividualEvaluated individual, Problem problem,
			IAgentLogger logger) throws ProblemToolException;
	
	public Individual getNeighbor(Individual individual, Problem problem,
			long neighborIndex, IAgentLogger logger) throws ProblemToolException;
	public IndividualEvaluated getNeighborEval(Individual individual, Problem problem,
			long neighborIndex, IAgentLogger logger) throws ProblemToolException;
	
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Problem problem, IAgentLogger logger)
			throws ProblemToolException;
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			Problem problem, IAgentLogger logger)
			throws ProblemToolException;
	public IndividualEvaluated[] createNewIndividualEval(Individual individual1, 
			Individual individual2, Individual individual3,
			Problem problem, IAgentLogger logger)
			throws ProblemToolException;	
}
