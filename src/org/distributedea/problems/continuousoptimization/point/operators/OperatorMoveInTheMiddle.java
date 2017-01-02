package org.distributedea.problems.continuousoptimization.point.operators;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problem.continuousoptimization.Interval;

public class OperatorMoveInTheMiddle {

	public static Individual[] create(IndividualPoint individualP1,
			IndividualPoint individualP2, IndividualPoint individualP3,
			Problem problem, IAgentLogger logger)
			throws Exception {
		
		double F = 1.0;
		
		ProblemContinuousOpt problemCO = (ProblemContinuousOpt) problem;
		
		List<Double> coordinates = new ArrayList<Double>();
		for (int i = 0; i < individualP1.getCoordinates().size(); i++) {
			
			double indACoorI = individualP1.exportCoordinate(i);
			double indBCoorI = individualP2.exportCoordinate(i);
			double indCCoorI = individualP3.exportCoordinate(i);
			
			double valueNew = indACoorI+F*(indBCoorI-indCCoorI);
			
			Interval intervalI = problemCO.getIntervals().get(i);
			
			// corrects the new computed value
			double intervalSize = intervalI.getMax() - intervalI.getMin();
			if (valueNew < intervalI.getMin()) {
				valueNew += intervalSize;
			}
			if (intervalI.getMax() < valueNew) {
				valueNew -= intervalSize;
			}
			
			coordinates.add(valueNew);
		}
				
		Individual[] individuals = {new IndividualPoint(coordinates)};
		return individuals;
	}

}
