package org.distributedea.problems.continuousoptimization.point.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.continuousoptimization.DomainDefinition;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;

public class ToolGenerateNextIndividualCO {
	
	public static Individual create(IndividualPoint individualP,
			int dimension, DomainDefinition domainDef, double maxStep,
			IAgentLogger logger) {
				
		List<Double> coordinatesNew = new ArrayList<Double>();
		
		for (int i = 0; i < dimension; i++) {
		
			Interval intervalI = domainDef.exportRestriction(i);
			double coordinateI = individualP.exportCoordinate(i);
			
			if (coordinateI + maxStep < intervalI.getMax()) {
								
				double coordinateNewI =
						coordinateI + maxStep*Math.random();
				coordinatesNew.add(coordinateNewI);
			} else {
				coordinatesNew.add(coordinateI);
			}
		}

		return new IndividualPoint(coordinatesNew);
	}

}
