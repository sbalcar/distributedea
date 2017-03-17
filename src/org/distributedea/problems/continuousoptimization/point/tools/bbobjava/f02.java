package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f02 {

	public static double evaluate(IndividualPoint x) {
	
		int d = x.getCoordinates().size();
		
		double result = 0;
		for (int i = 0; i < d; i++) {
			
			double zI = x.exportCoordinate(i);
			double exponent = 6*i/(d-1);
			
			result += Math.pow(10, exponent) * Math.pow(zI, 2);
		}
		
		return result;
	}
}
