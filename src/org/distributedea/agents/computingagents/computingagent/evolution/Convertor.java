package org.distributedea.agents.computingagents.computingagent.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
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
			Configuration conf) throws InvalidConfigurationException {
	
		if (individual instanceof IndividualPermutation) {
			
			IndividualPermutation individualPermutation =
					(IndividualPermutation) individual;
			return convertToIChromosome(individualPermutation, conf);
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
			IndividualPermutation individual, Configuration conf)
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

	/**
	 * Converts jgap IChromosome to general individual
	 * @param chromosome
	 * @param conf
	 * @return
	 * @throws InvalidConfigurationException
	 */
	public static Individual convertToIndividual(IChromosome chromosome,
			Configuration conf) throws InvalidConfigurationException {
	
		Individual convertedIndividual = null;
		
		if (chromosome instanceof Chromosome) {
			
			Chromosome chromosomeCh =
					(Chromosome) chromosome;
			convertedIndividual = convertToIndividualPermutation(chromosomeCh, conf);
		}
		
		if (convertedIndividual == null ||
				! convertedIndividual.validation()) {
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
		
		IndividualPermutation individual = new IndividualPermutation();
		individual.setPermutation(permutation);
		
		return individual;
	}
}
