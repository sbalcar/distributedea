
package org.distributedea.problems.continuousoptimization.point.operators;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;

public class OperatorRandomMove {
	
	public static Individual create(IndividualPoint individualPoint, double maxStep, IAgentLogger logger) {
		
		IndividualPoint individualNew = new IndividualPoint();

		for (int i = 0; i < individualPoint.getCoordinates().size(); i++) {
			
			double coordinateI = individualPoint.exportCoordinate(i);
			double coordinateNewI = coordinateI + maxStep*Math.random() - maxStep/2;
		
			individualNew.importCoordinate(i, coordinateNewI);
		}
		return individualNew;
	}
}
