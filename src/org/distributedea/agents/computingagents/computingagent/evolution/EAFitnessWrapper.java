package org.distributedea.agents.computingagents.computingagent.evolution;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;

/**
 * Wrapper for jgap Fitness operator
 * @author stepan
 *
 */
public class EAFitnessWrapper extends FitnessFunction {

	private static final long serialVersionUID = 1L;
	
	private Configuration conf;
	private boolean isMaximalization;
	private Problem problem;
	private ProblemTool problemTool;
	private AgentLogger logger;
	
	public EAFitnessWrapper(Configuration conf, boolean isMaximalization,
			Problem problem, ProblemTool problemTool, AgentLogger logger) {
		this.conf = conf;
		this.isMaximalization = isMaximalization;
		this.problem = problem;
		this.problemTool = problemTool;
		this.logger = logger;
	}
	
	@Override
	protected double evaluate(IChromosome chromosome) {
		
		Individual individual = null;
		try {
			individual = Convertor.convertToIndividual(chromosome, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Invalid Configuration", e);
			return -1;
		}
		
		if (isMaximalization) {
			return problemTool.fitness(individual, problem, logger);
		} else {
			return 1 / problemTool.fitness(individual, problem, logger);
		}
	}

}
