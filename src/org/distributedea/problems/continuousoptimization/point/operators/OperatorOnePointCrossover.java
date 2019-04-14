package org.distributedea.problems.continuousoptimization.point.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;

public class OperatorOnePointCrossover {

	public static IndividualPoint [] create(IndividualPoint individualP1,
			IndividualPoint individualP2, IAgentLogger logger) {

		int dimension = individualP1.getCoordinates().size();
		
		Random random = new Random();
		int indexOfPoint = random.nextInt(dimension);
		
		List<Double> coordinates1 = new ArrayList<>();
		List<Double> coordinates2 = new ArrayList<>();
		
		for (int i = 0; i < dimension; i++) {
			
			double coordinate1 = individualP1.exportCoordinate(i);
			double coordinate2 = individualP2.exportCoordinate(i);
			
			if (i < indexOfPoint) {
				coordinates1.add(coordinate1);
				coordinates2.add(coordinate2);
			} else {
				coordinates1.add(coordinate2);
				coordinates2.add(coordinate1);				
			}			
		}
		
		IndividualPoint individualPNew1 = new IndividualPoint(coordinates1);
		IndividualPoint individualPNew2 = new IndividualPoint(coordinates2);
		
		IndividualPoint[] individuals = {individualPNew1, individualPNew2};
		return individuals;
	}

}
