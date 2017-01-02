package org.distributedea.problems.continuousoptimization.point.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.continuousoptimization.Interval;

public class ToolGenerateNextIndividualCO {

	private static double maxStep = 0.005;
	
	public static Individual create(Problem problem,
			Individual individual, IAgentLogger logger) {
		
		ProblemContinuousOpt problemCO = (ProblemContinuousOpt) problem;
		IndividualPoint individualP = (IndividualPoint) individual;
		
		List<Interval> intervals = problemCO.getIntervals();
		List<Double> coordinates = individualP.getCoordinates();
				
		List<Double> coordinatesNew = new ArrayList<Double>();
		
		boolean isTheLastIndividual = true;
		for (int i = 0; i < intervals.size(); i++) {
		
			Interval intervalI = intervals.get(i);
			double coordinateI = coordinates.get(i);
			
			if (coordinateI + maxStep < intervalI.getMax()) {
				
				isTheLastIndividual = false;
				
				int sequenceNumber = (int) (coordinateI / maxStep);
				double coordinateNewI =
						maxStep*(sequenceNumber +1) + maxStep*Math.random();
				coordinatesNew.add(coordinateNewI);
			} else {
				coordinatesNew.add(coordinateI);
			}
		}

		if (isTheLastIndividual) {
			return null;
		}

		return new IndividualPoint(coordinatesNew);
	}

}
