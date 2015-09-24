package org.distributedea.agents.computingagents.computingagent.evolution;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;

public class Convertor {


	public static IChromosome convertToIChromosome(Individual individual, Configuration conf) throws InvalidConfigurationException {
	
		if (individual instanceof IndividualPermutation) {
			
			IndividualPermutation individualPermutation =
					(IndividualPermutation) individual;
			return convertToIChromosome(individualPermutation, conf);
		}
		
		return null;
	}
	
	private static Chromosome convertToIChromosome(IndividualPermutation individual, Configuration conf) throws InvalidConfigurationException {
		
		Gene[] sampleGenes = new Gene[individual.sizeOfPermutation()];
		for (int i = 0; i < sampleGenes.length; i++) {
			sampleGenes[i] = new IntegerGene(conf, 0, sampleGenes.length -1);
		}
		
		for (int numberIndex = 0;
				numberIndex < individual.sizeOfPermutation(); numberIndex++) {
			
			Gene geneI = sampleGenes[numberIndex];
			geneI.setAllele(
					individual.getPermutation().get(numberIndex));
		}
		
		return new Chromosome(conf, sampleGenes);
	}

	public static Individual convertToIndividual(IChromosome chromosome, Configuration conf) throws InvalidConfigurationException {
	
		if (chromosome instanceof Chromosome) {
			
			Chromosome chromosomeCh =
					(Chromosome) chromosome;
			return convertToIndividualPermutation(chromosomeCh, conf);
		}
		
		return null;
	}
	
	private static IndividualPermutation convertToIndividualPermutation(Chromosome chromosome, Configuration conf) throws InvalidConfigurationException {
		
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
