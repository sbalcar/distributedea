package org.distributedea.agents.computingagents.computingagent.evolution;

import java.util.List;
import java.util.Random;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.StockRandomGenerator;

public class EACrossoverWrapper implements GeneticOperator {

	private static final long serialVersionUID = 1L;

	private double crossRate;
	private Problem problem;
	private ProblemTool problemTool;
	private Configuration conf;
	private AgentLogger logger;
	
	public EACrossoverWrapper(double crossRate, Problem problem,
			ProblemTool problemTool, Configuration conf, AgentLogger logger) {
		
		this.crossRate = crossRate;
		this.problem = problem;
		this.problemTool = problemTool;
		this.conf = conf;
		this.logger = logger;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void operate(Population a_population, List a_candidateChromosomes) {
		
		int popSize = conf.getPopulationSize();
		Random random = new StockRandomGenerator();
		
		for(int i = 0; i < popSize; i+=2) {
			
			IChromosome chromCandidate1a = (IChromosome)
					a_population.getChromosome(random.nextInt(popSize));
			IChromosome chromCandidate1b = (IChromosome)
					a_population.getChromosome(random.nextInt(popSize));
			
			IChromosome chromosome1 = null;
			if (chromCandidate1a.getFitnessValue() > chromCandidate1b.getFitnessValue()) {
				chromosome1 = (IChromosome) chromCandidate1a.clone();
			} else {
				chromosome1 = (Chromosome)chromCandidate1b.clone();
			}
			
			IChromosome chromCandidate2a = (IChromosome)
					a_population.getChromosome(random.nextInt(popSize));
			IChromosome chromCandidate2b = (IChromosome)
					a_population.getChromosome(random.nextInt(popSize));
			
			IChromosome chromosome2 = null;
			if (chromCandidate2a.getFitnessValue() > chromCandidate2b.getFitnessValue()) { 
				chromosome2 = (IChromosome)chromCandidate2a.clone();
			} else {
				chromosome2 = (IChromosome)chromCandidate2b.clone();
			}
			
			if (random.nextDouble() < crossRate) {
				
				if (chromosome1 instanceof Chromosome &&
						chromosome2 instanceof Chromosome) {
					cross(chromosome1, chromosome2);
				}//TODO: for another representation
			}
			
			a_candidateChromosomes.add(chromosome1);
			a_candidateChromosomes.add(chromosome2);
		}
		
	}

	private void cross(IChromosome chromosome1, IChromosome chromosome2) {
		
		// converting the Chromosome1 to Individual
		Individual individualPerm1 = null;
		try {
			individualPerm1 = Convertor.convertToIndividual(
					chromosome1, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Can't convert Chromosome to Individual", e);
			return;
		}
		
		// converting the Chromosome2 to Individual
		Individual individualPerm2 = null;
		try {
			individualPerm2 = Convertor.convertToIndividual(
					chromosome2, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Can't convert Chromosome to Individual", e);
			return;
		}
		
		Individual newIndividual = problemTool.createNewIndividual(
				individualPerm1, individualPerm2, problem, logger);
		
		IChromosome chromosomeNew = null;
		try {
			chromosomeNew = Convertor.convertToIChromosome(
					newIndividual, conf);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Can't convert Individual to Chromosome", e);
			return;
		}
		
		Gene[] a_genes = chromosomeNew.getGenes();
		try {
			chromosome1.setGenes(a_genes);
		} catch (InvalidConfigurationException e) {
			logger.logThrowable("Can't set genes to Chromosome", e);
		}
		
	}

}
