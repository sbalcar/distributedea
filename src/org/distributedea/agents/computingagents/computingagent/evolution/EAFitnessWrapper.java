package org.distributedea.agents.computingagents.computingagent.evolution;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.IProblemTool;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;

/**
 * Wrapper for JGAP Fitness operator
 * @author stepan
 *
 */
public class EAFitnessWrapper extends FitnessFunction {

	private static final long serialVersionUID = 1L;
	
	private Configuration conf;
	private Problem problem;
	private IProblemTool problemTool;
	private AgentLogger logger;
	
	public EAFitnessWrapper(Configuration conf, Problem problem,
			IProblemTool problemTool, AgentLogger logger) {
		this.conf = conf;
		this.problem = problem;
		this.problemTool = problemTool;
		this.logger = logger;
	}
	
	@Override
	protected double evaluate(IChromosome chromosome) {
		
		Individual individual = null;
		try {
			individual = Convertor.convertToIndividual(chromosome, problem, problemTool, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Invalid Configuration", e);
			return -1;
		}
		
		if (problem.isMaximizationProblem()) {
			return problemTool.fitness(individual, problem, logger);
		} else {
			return 1 / problemTool.fitness(individual, problem, logger);
		}
	}

}
