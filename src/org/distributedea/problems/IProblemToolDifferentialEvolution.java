package org.distributedea.problems;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;


public interface IProblemToolDifferentialEvolution extends IProblemTool {
	
	public Individual differentialOfIndividuals(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception;
	
	public Individual cross(Individual individualOld,
			Individual individualNew, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception;


	default IndividualEvaluated differentialOfIndividualsEval(IndividualEvaluated individual1, 
			IndividualEvaluated individual2, IndividualEvaluated individual3,
			IProblem problem, Dataset dataset, PedigreeParameters pedigreeParams,
			IAgentLogger logger) throws Exception {

		Pedigree pedigree1 = individual1.getPedigree();
		Pedigree pedigree2 = individual2.getPedigree();
		Pedigree pedigree3 = individual3.getPedigree();
		
		Individual indiv1 = individual1.getIndividual();
		Individual indiv2 = individual2.getIndividual();
		Individual indiv3 = individual3.getIndividual();
				
		Individual individualNew = differentialOfIndividuals(indiv1, indiv2,
				indiv3, problem, dataset, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);

		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree1, pedigree2, pedigree3, pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);
	}
	
	default IndividualEvaluated cross(IndividualEvaluated individualEvalOld,
			IndividualEvaluated individualEvalNew, IProblem problem,
			Dataset dataset, PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws Exception {
		
		Individual individualOld = individualEvalOld.getIndividual();
		Pedigree pedigreeOld = individualEvalOld.getPedigree();
				
		Individual individualNew = individualEvalNew.getIndividual();
		Pedigree pedigreeNew = individualEvalNew.getPedigree();
		
		Individual individualCreated = cross(individualOld, individualNew,
				problem, dataset, logger);
		double fitnessCreated = fitness(individualCreated, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeCreated = pedigreeDef.create(pedigreeOld, pedigreeNew, pedigreeParams, logger);
				
		return new IndividualEvaluated(individualCreated, fitnessCreated, pedigreeCreated);
	}
	
}
