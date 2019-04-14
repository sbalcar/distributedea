package org.distributedea.problems;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;

public interface IProblemToolEvolution extends IProblemTool {

	/**
	 * Tries to improve given {@link Individual}. Improving fitness is not guaranteed.
	 * @param individual
	 * @param problem
	 * @param logger
	 * @return
	 * @throws ProblemToolException
	 */  
	public Individual mutationOfIndividual(Individual individual,
			IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception;
	
	public Individual[] crossIndividual(Individual individual1,
			Individual individual2, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception;
	
	
	default IndividualEvaluated mutationOfIndividualEval(IndividualEvaluated individualEval1,
			IProblem problem, Dataset dataset, PedigreeParameters pedigreeParams,
			IAgentLogger logger) throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
		
		Individual individualNew = mutationOfIndividual(individual1, problem, dataset, logger);
		if (individualNew.equals(individual1)) {
			return individualEval1.deepClone();
		}
		
		double fitness = fitness(individualNew, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree1, pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);
	}
	
	default IndividualEvaluated[] crossIndividualEval(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblem problem,
			Dataset dataset, PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
				
		Individual individual2 = individualEval2.getIndividual();
		Pedigree pedigree2 = individualEval2.getPedigree();
		
		Individual[] individualNew = crossIndividual(individual1,
				individual2, problem, dataset, logger);

		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree1, pedigree2, pedigreeParams, logger);
				
		IndividualEvaluated[] list = new IndividualEvaluated[individualNew.length];
		for (int i = 0; i < individualNew.length; i++) {
			
			Individual individualNewI = individualNew[i];
			if (individualNewI.equals(individual1)) {
				list[i] = individualEval1.deepClone();
				
			} else if (individualNewI.equals(individual2)) {
				list[i] = individualEval2.deepClone();
				
			} else {
				double fitnessI = fitness(individualNewI, problem, dataset, logger);
				list[i] = new IndividualEvaluated(individualNewI, fitnessI, pedigreeNew);
			}
		}
		return list;
	}

}
