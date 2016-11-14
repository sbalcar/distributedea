package org.distributedea.problems;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
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
	 * Evaluates given {@link Individual} as solution of given {@link Problem}
	 * @param individual
	 * @param problem
	 * @param logger
	 * @return
	 */
	public double fitness(Individual individual, Problem problem,
			IAgentLogger logger);
	
	/**
	 * Generated random {@link Individual} as solution for given {@link Problem}
	 * @param problem
	 * @param logger
	 * @return
	 */
	public IndividualEvaluated generateIndividualEval(Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger);
	
	public IndividualEvaluated generateFirstIndividualEval(Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger);
	
	public IndividualEvaluated generateNextIndividualEval(Problem problem,
			IndividualEvaluated individual, PedigreeParameters pedigreeParams,
			IAgentLogger logger);
	
	
	/**
	 * Tries to improve given {@link Individual}. Improving fitness is not guaranteed.
	 * @param individual
	 * @param problem
	 * @param logger
	 * @return
	 * @throws ProblemToolException
	 */  
	public IndividualEvaluated improveIndividualEval(IndividualEvaluated individual, Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws ProblemToolException;
	
	public IndividualEvaluated getNeighborEval(IndividualEvaluated individual,
			Problem problem, long neighborIndex, PedigreeParameters pedigreeParams,
			IAgentLogger logger) throws ProblemToolException;
	
	public IndividualEvaluated[] createNewIndividual(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws ProblemToolException;
	

	public IndividualEvaluated[] createNewIndividualEval(IndividualEvaluated individual1, 
			IndividualEvaluated individual2, IndividualEvaluated individual3,
			Problem problem, PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws ProblemToolException;	
}
