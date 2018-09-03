package org.distributedea.problemtools;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;

public interface IProblemToolTabuSearch extends IProblemTool {

	public Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset, long neighborIndex,
			IAgentLogger logger) throws Exception;
	
	
	default IndividualEvaluated getNeighborEval(IndividualEvaluated individualEval,
			IProblem problem, Dataset dataset, long neighborIndex,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception {
		
		Individual individual = individualEval.getIndividual();
		Pedigree pedigree = individualEval.getPedigree();
		
		Individual individualNew = getNeighbor(individual, problem, dataset,
				neighborIndex, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);

		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree, pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);
	}
}
