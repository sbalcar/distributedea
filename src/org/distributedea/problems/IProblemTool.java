package org.distributedea.problems;

import java.io.File;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.IProblem;

/**
 * Interface for {@link AProblemTool} implemntation
 * @author stepan
 *
 */
public interface IProblemTool {

	/**
	 * Export arguments
	 * @return
	 */
	public Arguments exportArguments();
	
	/**
	 * Import arguments
	 * @param arguments
	 */
	public void importArguments(Arguments arguments);
	
	
	/**
	 * Returns {@link List} of agents that can use the tool
	 * @return
	 */
	public List<Class<?>> belongsToAgent();
	
	/**
	 * Returns Class of {@link Problem} which this {@link AProblemTool} serves
	 * @return
	 */
	public Class<?> problemReprezentation();
	
	/**
	 * Returns Class of {@link Dataset} which this {@link AProblemTool} serves
	 * @return
	 */
	public Class<?> datasetReprezentation();
	
	/**
	 * Returns Class of {@link Individual} for which provides tool operations
	 * @return
	 */
	public Class<?> reprezentationWhichUses();
	
	/**
	 * Initialization of {@link AProblemTool}
	 * @param dataset
	 * @param agentConf
	 * @param logger
	 * @throws ProblemToolException
	 */
	public void initialization(IProblem problem, Dataset dataset, AgentConfiguration agentConf,
			MethodIDs methodIDs, IAgentLogger logger) throws Exception;
	
	/**
	 * Exit of {@link AProblemTool}
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
	public Dataset readDataset(IDatasetDescription datasetDescription, IProblem problem, IAgentLogger logger);
	
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
	public double fitness(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger);
	
	/**
	 * Generated random {@link Individual} as solution for given {@link Problem}
	 * @param dataset
	 * @param logger
	 * @return
	 */
	public IndividualEvaluated generateIndividualEval(IProblem problem,
			Dataset dataset, PedigreeParameters pedigreeParams, IAgentLogger logger);
	
}
