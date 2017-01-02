package org.distributedea.agents.computingagents.computingagent.evolutionjgap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.continuousoptimization.Interval;
import org.distributedea.problems.IProblemTool;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;

/**
 * Converter of jgap representation to ontological representation
 * @author stepan
 *
 */
public class Convertor {

	/**
	 * Converts general individual to jgap IChromosome
	 * @param individual
	 * @param conf
	 * @return
	 * @throws InvalidConfigurationException
	 */
	public static IChromosome convertToIChromosome(Individual individual,
			Problem problem, Configuration conf) throws InvalidConfigurationException {
	
		if (individual instanceof IndividualPermutation) {
			
			IndividualPermutation individualPermutation =
					(IndividualPermutation) individual;
			return convertToIChromosome(individualPermutation, null, conf);
			
		} else if (individual instanceof IndividualPoint) {
			
			IndividualPoint individualPoint =
					(IndividualPoint)individual;
			
			ProblemContinuousOpt problemCO =
					(ProblemContinuousOpt)problem;
			
			return convertToIChromosome(individualPoint, problemCO, conf);
		}
		
		return null;
	}
	
	/**
	 * Converts permutation based individual to jgap Chromosome
	 * @param individual
	 * @param conf
	 * @return
	 * @throws InvalidConfigurationException
	 */
	private static Chromosome convertToIChromosome(
			IndividualPermutation individual, Problem problem,
			Configuration conf)
			throws InvalidConfigurationException {
		
		Gene[] sampleGenes = new Gene[individual.sizeOfPermutation()];
		List<Integer> permutation = individual.getPermutation();
		
		int minVal = Collections.min(permutation);
		int maxVal = Collections.max(permutation);
		
		for (int i = 0; i < sampleGenes.length; i++) {
			sampleGenes[i] = new IntegerGene(conf, minVal, maxVal);
		}
		
		for (int numberIndex = 0;
				numberIndex < individual.sizeOfPermutation(); numberIndex++) {
			Gene geneI = sampleGenes[numberIndex];
			geneI.setAllele(permutation.get(numberIndex));
		}
		
		return new Chromosome(conf, sampleGenes);
	}

	private static Chromosome convertToIChromosome(
			IndividualPoint individual, ProblemContinuousOpt problem,
			Configuration conf)
			throws InvalidConfigurationException {
		
		List<Interval> intervals = problem.getIntervals();
		
		Gene[] sampleGenes = new Gene[problem.getDimension()];
		for (int i = 0; i < sampleGenes.length; i++) {

			Interval intervalI = intervals.get(i);
			
			int minVal = (int) intervalI.getMin();
			int maxVal = (int) intervalI.getMax();
			
			sampleGenes[i] = new DoubleGene(conf, minVal, maxVal);
		}
		
		List<Double> coordinates = individual.getCoordinates();
		
		for (int numberIndex = 0;
				numberIndex < coordinates.size(); numberIndex++) {
			Gene geneI = sampleGenes[numberIndex];
			geneI.setAllele(coordinates.get(numberIndex));
		}
		
		return new Chromosome(conf, sampleGenes);
	}
	/**
	 * Converts jgap IChromosome to general individual
	 * @param chromosome
	 * @param conf
	 * @return
	 * @throws InvalidConfigurationException
	 */
	public static Individual convertToIndividual(IChromosome chromosome,
			Problem problem, IProblemTool problemTool, Configuration conf) throws InvalidConfigurationException {
	
		Individual convertedIndividual = null;
		
		Class<?> reprezentation = problemTool.reprezentationWhichUses();
		
		Chromosome chromosomeCh =
				(Chromosome) chromosome;
		
		if (reprezentation == IndividualPoint.class) {
			
			convertedIndividual =
					convertToIndividualPoint(chromosomeCh, conf);
			
		} else if (reprezentation == IndividualPermutation.class) {
			
			convertedIndividual =
					convertToIndividualPermutation(chromosomeCh, conf);
		}
		
		
		if (convertedIndividual == null ||
				! convertedIndividual.valid(new TrashLogger())) {
			throw new IllegalStateException("Conversion is not valid");
		}
		
		return convertedIndividual;
	}
	
	/**
	 * Converts jgap Chromosome to permutation based individual
	 * @param chromosome
	 * @param conf
	 * @return
	 * @throws InvalidConfigurationException
	 */
	private static IndividualPermutation convertToIndividualPermutation(
			Chromosome chromosome, Configuration conf)
			throws InvalidConfigurationException {
		
		Gene[] genes = chromosome.getGenes();
		
		List<Integer> permutation = new ArrayList<>();
		for (int i = 0; i < genes.length; i++) {
			Gene genI = genes[i];
			
			int genValueI = (int) genI.getAllele();
			permutation.add(genValueI);
		}
		
		return new IndividualPermutation(permutation);
	}
	
	private static IndividualPoint convertToIndividualPoint(
			Chromosome chromosome, Configuration conf)
			throws InvalidConfigurationException {
		
		Gene[] genes = chromosome.getGenes();
		
		List<Double> coordinates = new ArrayList<>();
		for (int i = 0; i < genes.length; i++) {
			Gene genI = genes[i];
			
			double genValueI = (double) genI.getAllele();
			coordinates.add(genValueI);
		}
		
		return new IndividualPoint(coordinates);
	}
	
}
