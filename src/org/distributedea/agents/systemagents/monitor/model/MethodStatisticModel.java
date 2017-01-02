package org.distributedea.agents.systemagents.monitor.model;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;

/**
 * Structure represents model for one Method
 * @author stepan
 *
 */
public class MethodStatisticModel {
	
	private final MethodDescription agentDescription;
	
	private IndividualEvaluated bestIndividual;
	
	private int numberOfIndividuals = 0;
	private int numberOfBestCreatedIndividuals = 0;
	private int numberOfBestCreatedGeneticMaterial = 0;
	
	
	private int INDIVIDUALS_MAX_SIZE = 1000;
	private int duplicateIndividualsNumber = 0;
	private List<IndividualDescription> individuals = new ArrayList<>();
	

	/**
	 * Constructor
	 * @param agentDescription
	 */
	public MethodStatisticModel(MethodDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		
		this.agentDescription = agentDescription;
	}
	
	public MethodDescription getAgentDescription() {
		return agentDescription;
	}
	
	public IndividualEvaluated getBestIndividual() {
		return this.bestIndividual;
	}

	public int getNumberOfIndividuals() {
		return this.numberOfIndividuals;
	}
	public void incrementIndividualsCount() {
		this.numberOfIndividuals++;
	}	
	
	
	public void incrementBestCreatedIndividualsCount() {
		this.numberOfBestCreatedIndividuals++;
	}
	public int getBestCreatedIndividualsCount() {
		return this.numberOfBestCreatedIndividuals;
	}
	
	
	public void incrementBestCreatedGeneticMaterialCount() {
		this.numberOfBestCreatedGeneticMaterial++;
	}
	public int getBestCreatedGeneticMaterialCount() {
		return this.numberOfBestCreatedGeneticMaterial;
	}
	

	void addBestIndividualEvaluatedFromLastIteration(
			IndividualEvaluated individualEval) {
		
		if (bestIndividual == null) {
			addIndividualEvaluated(individualEval);
			
			numberOfBestCreatedIndividuals = 0;
			numberOfBestCreatedGeneticMaterial = 0;
		}
	}
	public void addIndividualEvaluated(IndividualEvaluated individualEval) {

		// update best agent of method
		if ((bestIndividual == null) ||
			isFistIndividualWBetterThanSecond(individualEval, bestIndividual)) {
			
			bestIndividual = individualEval;
		}
		
		// transform Individual to IndividualDescription
		double fitness = individualEval.getFitness();
		Individual individual = individualEval.getIndividual();
		
		String individualHash = individual.toLogString();
		List<Double> distances = null;
		
		IndividualDescription description = new IndividualDescription();
		description.setIndividualHash(individualHash);
		description.setFitness(fitness);
		description.setDistances(distances);
		
		if (individuals.size() <= INDIVIDUALS_MAX_SIZE) {
			if (individuals.contains(description)) {
				duplicateIndividualsNumber++;
			} else {
				individuals.add(description);
			}
		}		
		incrementIndividualsCount();
	}

	public double fitnessAverage() {
		
		if (individuals == null) {
			return 0.0;
		}
		
		double fitnessAverage = 0;
		for (IndividualDescription individualDescI : individuals) {
			
			double fitnessRatio =
					individualDescI.getFitness() / individuals.size();
			fitnessAverage += fitnessRatio;
		}
		return fitnessAverage;
	}
	
	private boolean isFistIndividualWBetterThanSecond(
			IndividualEvaluated individual1, IndividualEvaluated individual2) {
		
		return FitnessTool.isFistIndividualEBetterThanSecond(individual1,
						individual2, agentDescription.getProblemDefinition());
	}
	
	public MethodStatistic exportMethodStatistic() {
		
		MethodStatisticResult methodStatisticResult = new MethodStatisticResult();
		methodStatisticResult.setNumberOfIndividuals(
				getNumberOfIndividuals());
		methodStatisticResult.setNumberOfTheBestCreatedIndividuals(
				getBestCreatedIndividualsCount());
		methodStatisticResult.setNumberOfGoodCreatedMaterial(
				getBestCreatedGeneticMaterialCount());
		
		methodStatisticResult.setBestIndividual(getBestIndividual());
		methodStatisticResult.setFitnessAverage(fitnessAverage());
		
		double ratio = getNumberOfIndividuals() /
				(individuals.size() + duplicateIndividualsNumber);
		int typeOfIndividualsEstimate = (int) (ratio * individuals.size());
		methodStatisticResult.setNumberOfTypeIndividuals(
				typeOfIndividualsEstimate);
		
		MethodStatistic methodStatistic = new MethodStatistic(
				getAgentDescription(), methodStatisticResult);
		
		return methodStatistic;
	}
}
