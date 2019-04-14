package org.distributedea.problems.evcharging.point.tools;


import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;

public class ToolGenerateIndividualEVCharging {

	public static Individual generateIndividual(int dimension,
			Random randomn, double normalDistMultiplicator, IAgentLogger logger) {
		
		IndividualPoint individual = new IndividualPoint();
		for (int i = 0; i < dimension; i++) {
			
			double valI = randomn.nextGaussian() * normalDistMultiplicator;
			individual.importCoordinate(i, valI);
		}
		
		return individual;
	}
}
