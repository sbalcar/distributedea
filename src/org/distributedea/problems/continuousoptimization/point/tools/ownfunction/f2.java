package org.distributedea.problems.continuousoptimization.point.tools.ownfunction;

import java.util.List;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f2 {

	public static double evaluate(IndividualPoint individualPoint) {
		
		List<Double> coordinates = individualPoint.getCoordinates();
		double x = coordinates.get(0);
		double y = coordinates.get(1);
		
		return function(x, y);
	}
	
	private static double function(double x, double y) {
		
		return 100 * Math.pow((x*x - y), 2) + Math.pow((1 - x), 2);
	}
}
