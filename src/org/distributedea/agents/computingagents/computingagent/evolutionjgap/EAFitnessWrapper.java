package org.distributedea.agents.computingagents.computingagent.evolutionjgap;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
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
	private Dataset dataset;
	private IProblemDefinition problemDef;
	private IProblemTool problemTool;
	private IAgentLogger logger;
	
	public EAFitnessWrapper(Configuration conf, Dataset dataset,
			IProblemDefinition problemDef, IProblemTool problemTool,
			IAgentLogger logger) {
		this.conf = conf;
		this.dataset = dataset;
		this.problemDef = problemDef;
		this.problemTool = problemTool;
		this.logger = logger;
	}
	
	@Override
	protected double evaluate(IChromosome chromosome) {
		
		Individual individual = null;
		try {
			individual = Convertor.convertToIndividual(chromosome, dataset, problemTool, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Invalid Configuration", e);
			return -1;
		}
		
		if (problemDef.exportIsMaximizationProblem()) {
			return problemTool.fitness(individual, problemDef, dataset, logger);
		} else {
			return 1 / problemTool.fitness(individual, problemDef, dataset, logger);
		}
	}

}
