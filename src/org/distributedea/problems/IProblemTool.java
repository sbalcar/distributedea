package org.distributedea.problems;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
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
	public Class<?> problemReprezentation();
	
	/**
	 * Returns Class of {@link Dataset} which this {@link ProblemTool} serves
	 * @return
	 */
	public Class<?> datasetReprezentation();
	
	/**
	 * Returns Class of {@link Individual} for which provides tool operations
	 * @return
	 */
	public Class<?> reprezentationWhichUses();
	
	/**
	 * Initialization of {@link ProblemTool}
	 * @param dataset
	 * @param agentConf
	 * @param logger
	 * @throws ProblemToolException
	 */
	public void initialization(Dataset dataset, AgentConfiguration agentConf,
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
	public Dataset readDataset(File fileOfProblem, IAgentLogger logger);
	
	/**
	 * Reads instance of the Solution(Individual) from the file,
	 * for the illegal input file name returns null
	 * @param fileName
	 * @param dataset
	 * @param logger
	 * @return
	 */
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger);
	
	/**
	 * Evaluates given {@link Individual} as solution of given {@link Problem}
	 * @param individual
	 * @param dataset
	 * @param logger
	 * @return
	 */
	public double fitness(Individual individual, IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger);
	
	/**
	 * Generated random {@link Individual} as solution for given {@link Problem}
	 * @param dataset
	 * @param logger
	 * @return
	 */
	public IndividualEvaluated generateIndividualEval(IProblemDefinition problemDef,
			Dataset dataset, PedigreeParameters pedigreeParams, IAgentLogger logger);
	
	public IndividualEvaluated generateFirstIndividualEval(IProblemDefinition problemDef,
			Dataset dataset, PedigreeParameters pedigreeParams, IAgentLogger logger);
	
	public IndividualEvaluated generateNextIndividualEval(IProblemDefinition problemDef,
			Dataset dataset, IndividualEvaluated individual, PedigreeParameters pedigreeParams,
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
			IProblemDefinition problemDef, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception;
	
	public IndividualEvaluated getNeighborEval(IndividualEvaluated individual,
			IProblemDefinition problemDef, Dataset dataset, long neighborIndex,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception;
	
	public IndividualEvaluated[] createNewIndividual(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblemDefinition problemDef,
			Dataset dataset, PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws Exception;
	

	public IndividualEvaluated[] createNewIndividualEval(IndividualEvaluated individual1, 
			IndividualEvaluated individual2, IndividualEvaluated individual3,
			IProblemDefinition problemDef, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception;	
}
