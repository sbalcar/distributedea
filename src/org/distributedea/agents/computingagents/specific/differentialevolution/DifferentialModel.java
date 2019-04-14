package org.distributedea.agents.computingagents.specific.differentialevolution;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

public class DifferentialModel {
	
	private IndividualEvaluated[] population;
	
	private Random random = new Random();
	
	/**
	 * Constructor
	 * @param population0
	 * @param popSize
	 */
	public DifferentialModel(Vector<IndividualEvaluated> population0, int popSize) {
		if (population0 == null) {
			throw new IllegalArgumentException("Argument " +
					Vector.class.getSimpleName() + " is not valid");
		}
		if (popSize < 4 || popSize != population0.size()) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}

		
		this.population = new IndividualEvaluated[popSize];
		
		for (int i = 0; i < popSize; i++) {
			
			IndividualEvaluated indivI = population0.get(i);
			this.population[i] = indivI;
		}
	}

	public DifferentialQuaternion getQuaternion() {
	
		int popSize = population.length;
		
		//pick random point from population
		int candidateIndex = random.nextInt(popSize);
		
		int index1;
		do {
			index1 = random.nextInt(popSize);
		} while (index1 == candidateIndex);

		int index2;
		do {
			index2 = random.nextInt(popSize);
		} while (index2 == candidateIndex || index2 == index1);
		
		int index3;
		do {
			index3 = random.nextInt(popSize);
		} while (index3 == candidateIndex || index3 == index1 || index3 == index2);

		
		DifferentialQuaternion quaternion = new DifferentialQuaternion();
		quaternion.individualCandidateI = population[candidateIndex];
		
		quaternion.individual1 = population[index1];
		quaternion.individual2 = population[index2];
		quaternion.individual3 = population[index3];
		
		return quaternion;
	}

	public void replaceIndividual(IndividualEvaluated indivToDel,
			IndividualEvaluated indivToAdd) {
		
		if (contains(indivToAdd)) {
			return;
		}
		
		int index = indexOf(indivToDel);
		population[index] = indivToAdd;
	}
	
	public boolean contains(IndividualEvaluated individualEval) {
		
		return indexOf(individualEval) != -1;
	}
	
	private int indexOf(IndividualEvaluated indiv) {
		if (indiv == null) {
			new IllegalArgumentException("Argument is not valid");
		}
		
		for (int i = 0; i < population.length; i++) {
			if (indiv.equals(population[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public IndividualEvaluated getBestIndividual(IProblem problem) {

		Arrays.sort(population, new CmpIndividualEvaluated(problem));
		return population[0];
	}
}
