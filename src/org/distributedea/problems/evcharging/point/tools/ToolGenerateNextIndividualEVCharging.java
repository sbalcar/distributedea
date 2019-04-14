package org.distributedea.problems.evcharging.point.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;

public class ToolGenerateNextIndividualEVCharging {

	public static Individual create(IndividualPoint individualP,
			double minValue, double maxValue, double maxStep,
			Random randomn, IAgentLogger logger) {
		
		int dimension = individualP.getCoordinates().size();
		
		List<Double> coordinatesNew = new ArrayList<Double>();
		
		for (int i = 0; i < dimension; i++) {
		
			double coordinateI = individualP.exportCoordinate(i);
			
			double stepI = maxStep * Math.random();
			
			if (coordinateI + stepI < maxValue) {
				
				coordinatesNew.add(coordinateI + stepI);
			} else {
				
				coordinatesNew.add(randomn.nextGaussian());
			}
		}

		return new IndividualPoint(coordinatesNew);
	}

}
