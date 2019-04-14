package org.distributedea.problems;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;

public interface IProblemToolBruteForce extends IProblemTool {

	
	public Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger);
	
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger);
	
	
	default IndividualEvaluated generateFirstIndividualEval(
			IProblem problem, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individualNew = generateFirstIndividual(problem, dataset, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigree = pedigreeDef.create(pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
	default IndividualEvaluated generateNextIndividualEval(
			IProblem problem, Dataset dataset,
			IndividualEvaluated individualEval, long neighborIndex,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individual = individualEval.getIndividual();
		Pedigree pedigree = individualEval.getPedigree();
		
		Individual individualNew = generateNextIndividual(problem, dataset,
				individual, neighborIndex, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);

		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree, pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);
	}
}
