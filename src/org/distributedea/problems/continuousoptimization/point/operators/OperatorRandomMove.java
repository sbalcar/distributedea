package org.distributedea.problems.continuousoptimization.point.operators;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;

public class OperatorRandomMove {

	private static double maxStep = 0.005;
	
	public static Individual create(IndividualPoint individualPoint, IAgentLogger logger) {
		
		IndividualPoint individualNew = new IndividualPoint();

		for (int i = 0; i < individualPoint.getCoordinates().size(); i++) {
			
			double coordinateI = individualPoint.exportCoordinate(i);
			double coordinateNewI = coordinateI + maxStep*Math.random() - maxStep/2;
		
			individualNew.importCoordinate(i, coordinateNewI);
		}
		return individualNew;
	}
}
