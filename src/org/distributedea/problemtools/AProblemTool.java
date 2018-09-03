package org.distributedea.problemtools;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;

/**
 * Abstract class for {@link AProblemTool}
 * @author stepan
 *
 */
public abstract class AProblemTool implements IProblemTool {

	
	protected abstract Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger);
	
	public final IndividualEvaluated generateIndividualEval(
			IProblem problem, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individualNew = generateIndividual(problem, dataset, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigree = pedigreeDef.create(pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
}
