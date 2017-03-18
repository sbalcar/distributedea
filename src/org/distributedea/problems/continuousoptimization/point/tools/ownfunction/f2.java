package org.distributedea.problems.continuousoptimization.point.tools.ownfunction;

import java.util.List;

import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.problems.continuousoptimization.point.tools.bbobjava.IFuncitonCO;

public class f2 implements IFuncitonCO {

	@Override
	public void initialisation(int d) {
	}
	
	@Override
	public double evaluate(IndividualPoint individualPoint) {
		
		List<Double> coordinates = individualPoint.getCoordinates();
		double x = coordinates.get(0);
		double y = coordinates.get(1);
		
		return function(x, y);
	}
	
	private double function(double x, double y) {
		
		return 100 * Math.pow((x*x - y), 2) + Math.pow((1 - x), 2);
	}
	
}
