package org.distributedea.agents.computingagents.computingagent.evolutionjgap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemToolException;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;

/**
 * Wrapper for jgap Mutation operator
 * @author stepan
 *
 */
public class EAMutationWrapper implements GeneticOperator {

	private static final long serialVersionUID = 1L;

	private double mutationRate;
	private Problem problem;
	private IProblemTool problemTool;
	private Configuration conf;
	private AgentLogger logger;
	
	public EAMutationWrapper(double mutationRate, Problem problem,
			IProblemTool problemTool, Configuration conf, AgentLogger logger) {
		
		this.mutationRate = mutationRate;
		this.problem = problem;
		this.problemTool = problemTool;
		this.conf = conf;
		this.logger = logger;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void operate(Population aPopulation, List aCandidateChromosomes) {

		List<IChromosome> candidates = new ArrayList<>();
		
		List<IChromosome> chromosomes = aPopulation.getChromosomes();
		
		Random rand = new Random();
		
		for (IChromosome chromosomeI : chromosomes) {
			
			if (rand.nextDouble() > mutationRate)
				continue;
			
			IChromosome chromosomeCloneI = (IChromosome)
					chromosomeI.clone();
		
			IChromosome newChromosumeI = mutation(chromosomeCloneI);
		
			if (newChromosumeI == null) {
				candidates.add(null);
				continue;
			}
			
			// insert new Gene array to the cloned Chromosome
			Gene[] a_genes = newChromosumeI.getGenes();
			
			try {
				chromosomeCloneI.setGenes(a_genes);
			} catch (InvalidConfigurationException e) {
				logger.logThrowable("Configuration problem by changing genes in"
						+ "Chromosome", e);
				
				// returns empty population
				aCandidateChromosomes.clear();
				return;
			}
			
			candidates.add(chromosomeCloneI);
			chromosomeCloneI.setIsSelectedForNextGeneration(true);
		}		

		for (Object candidateI : candidates) {
			aCandidateChromosomes.add(candidateI);
		}
		
	}
	
	/**
	 * Mutation of Chromosome
	 * @param chromosume
	 * @return
	 */
	private IChromosome mutation(IChromosome chromosume) {
		
		// converting the Chromosome to Individual
		Individual individual = null;
		try {
			individual = Convertor.convertToIndividual(
					chromosume, problem, problemTool, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Can't convert Chromosome to Individual", e);
			return null;
		}
		
		double fitness = problemTool.fitness(individual, problem, logger);
		
		// call the mutation
		IndividualEvaluated individualNew;
		try {
			individualNew = problemTool.improveIndividualEval(
					new IndividualEvaluated(individual, fitness, null),
					problem, null, logger);
		} catch (ProblemToolException e1) {
			return null;
		}
		
		// converting the new Individual to Chromosome
		IChromosome newChromosumeI = null;
		try {
			newChromosumeI = Convertor.convertToIChromosome(
					individualNew.getIndividual(), problem, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Can't convert Individual to Chromosome", e);
			return null;
		}
		
		return newChromosumeI;
		
	}
	

}
