package org.distributedea.problems;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;

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
			IAgentLogger logger) throws Exception;
	
	/**
	 * Exit of {@link ProblemTool}
	 * @throws ProblemToolException
	 */
	public void exit() throws Exception;
	
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
	public double fitness(Individual individual, IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger);
	
	/**
	 * Generated random {@link Individual} as solution for given {@link Problem}
	 * @param problem
	 * @param logger
	 * @return
	 */
	public IndividualEvaluated generateIndividualEval(IProblemDefinition problemDef,
			Problem problem, PedigreeParameters pedigreeParams, IAgentLogger logger);
	
	public IndividualEvaluated generateFirstIndividualEval(IProblemDefinition problemDef,
			Problem problem, PedigreeParameters pedigreeParams, IAgentLogger logger);
	
	public IndividualEvaluated generateNextIndividualEval(IProblemDefinition problemDef,
			Problem problem, IndividualEvaluated individual, PedigreeParameters pedigreeParams,
			IAgentLogger logger);
	
	
	/**
	 * Tries to improve given {@link Individual}. Improving fitness is not guaranteed.
	 * @param individual
	 * @param problem
	 * @param logger
	 * @return
	 * @throws ProblemToolException
	 */  
	public IndividualEvaluated improveIndividualEval(IndividualEvaluated individual,
			IProblemDefinition problemDef, Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception;
	
	public IndividualEvaluated getNeighborEval(IndividualEvaluated individual,
			IProblemDefinition problemDef, Problem problem, long neighborIndex,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception;
	
	public IndividualEvaluated[] createNewIndividual(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblemDefinition problemDef,
			Problem problem, PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws Exception;
	

	public IndividualEvaluated[] createNewIndividualEval(IndividualEvaluated individual1, 
			IndividualEvaluated individual2, IndividualEvaluated individual3,
			IProblemDefinition problemDef, Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception;	
}
