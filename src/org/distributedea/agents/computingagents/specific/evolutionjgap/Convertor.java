package org.distributedea.agents.computingagents.specific.evolutionjgap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problemtools.IProblemTool;
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
			IProblem problem, Dataset dataset, Configuration conf
			) throws InvalidConfigurationException {
	
		ProblemContinuousOpt problemCO =
				(ProblemContinuousOpt) problem;
		DatasetContinuousOpt datasetCO =
				(DatasetContinuousOpt)dataset;

		
		if (individual instanceof IndividualPermutation) {
			
			IndividualPermutation individualPermutation =
					(IndividualPermutation) individual;
			return convertToIChromosome(individualPermutation, problemCO, datasetCO, conf);
			
		} else if (individual instanceof IndividualPoint) {
			
			IndividualPoint individualPoint =
					(IndividualPoint)individual;
						
			return convertToIChromosome(individualPoint, problemCO, datasetCO, conf);
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
			IndividualPermutation individual, ProblemContinuousOpt problemCO,
			DatasetContinuousOpt datasetCO, Configuration conf)
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
			IndividualPoint individual, ProblemContinuousOpt problemCO,
			DatasetContinuousOpt datasetCO, Configuration conf)
			throws InvalidConfigurationException {
		
		Gene[] sampleGenes = new Gene[problemCO.getDimension()];
		for (int i = 0; i < sampleGenes.length; i++) {

			Interval intervalI = datasetCO.getDomain().exportRestriction(i);
			
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
			Dataset dataset, IProblemTool problemTool, Configuration conf) throws InvalidConfigurationException {
	
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
