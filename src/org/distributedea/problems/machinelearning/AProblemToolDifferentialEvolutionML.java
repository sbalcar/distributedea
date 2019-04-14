package org.distributedea.problems.machinelearning;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemToolDifferentialEvolution;

public abstract class AProblemToolDifferentialEvolutionML extends AAProblemToolML implements IProblemToolDifferentialEvolution {

	public abstract Individual differentialOfIndividuals(Individual individual1,
			Individual individual2, Individual individual3,
			IProblem problem, Dataset dataset, IAgentLogger logger)
			throws Exception;
}
