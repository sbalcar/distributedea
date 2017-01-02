package org.distributedea.problems.continuousoptimization.point.tools.bbob;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;

public class ToolFitnessBbobCO {

	public static double evaluate(IndividualPoint individualPoint,
			IProblemDefinition problem, IJNIfgeneric fgeneric,
			BbobTools bbobTools, IAgentLogger logger) {
		
		if (individualPoint == null) {
			if (problem.exportIsMaximizationProblem()) {
				return Double.MIN_VALUE;
			} else {
				return Double.MAX_VALUE;
			}
		}
		
		List<Double> coordinatesList = individualPoint.getCoordinates();

		
		Double[] coordinatesArray = new Double[coordinatesList.size()];
		coordinatesList.toArray(coordinatesArray);
		
		double[] coordinates = ArrayUtils.toPrimitive(coordinatesArray);
		
		return fgeneric.evaluate(coordinates);

	}
}
