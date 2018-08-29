package org.distributedea.problemtools.continuousoptimization.point.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.ProblemContinuousOpt;

public class ToolGenerateNextIndividualCO {

	private static double maxStep = 0.005;
	
	public static Individual create(IndividualPoint individualP,
			ProblemContinuousOpt problemCO, DatasetContinuousOpt datasetCO,
			IAgentLogger logger) {
				
		List<Double> coordinatesNew = new ArrayList<Double>();
		
		for (int i = 0; i < problemCO.getDimension(); i++) {
		
			Interval intervalI = datasetCO.getDomain().exportRestriction(i);
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
