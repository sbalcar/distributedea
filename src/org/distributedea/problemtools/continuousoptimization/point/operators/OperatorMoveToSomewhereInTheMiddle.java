package org.distributedea.problemtools.continuousoptimization.point.operators;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;

public class OperatorMoveToSomewhereInTheMiddle {

	public static IndividualPoint [] create(IndividualPoint individualP1,
			IndividualPoint individualP2, IAgentLogger logger) {
		
		if (individualP1.getCoordinates().size() !=
				individualP2.getCoordinates().size()) {
			throw new IllegalStateException("Individuals doesn't have the same dimension");
		}

		IndividualPoint individualPNew1 = new IndividualPoint();
		IndividualPoint individualPNew2 = new IndividualPoint();
		
		for (int i = 0; i < individualP1.getCoordinates().size(); i++) {
			
			double coordinate1 = individualP1.exportCoordinate(i);
			double coordinate2 = individualP2.exportCoordinate(i);
			
			double coordinatemin = Math.min(coordinate1, coordinate2);
			double distance = Math.abs(coordinate1 -coordinate2);
			
			// somewhere between
			double coordinateNew1 = coordinatemin + Math.random()*distance;
			individualPNew1.importCoordinate(i, coordinateNew1);
			
			double coordinateNew2 = coordinatemin + Math.random()*distance;
			individualPNew2.importCoordinate(i, coordinateNew2);
		}
		
		IndividualPoint[] individuals = {individualPNew1, individualPNew2};
		return individuals;

	}
}
