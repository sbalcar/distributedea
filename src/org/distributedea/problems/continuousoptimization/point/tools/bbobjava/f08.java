package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f08 {

	public static double evaluate(IndividualPoint individualPoint) {
		
		int d = individualPoint.getCoordinates().size();
		
		double result = 0;
		for (int i = 0; i <= d -2; i++) {

			IndividualPoint z = countZ(individualPoint);
			
			double zi = z.exportCoordinate(i);
			double ziplus1 = z.exportCoordinate(i+1);
			
			result += 100* Math.pow(Math.pow(zi, 2) -ziplus1, 2) + Math.pow(zi -1, 2);
		}
		
		return result;
	}
	
	private static IndividualPoint countZ(IndividualPoint x) {
		
		int d = x.getCoordinates().size();
		
		double max = Math.max(1, Math.sqrt(d) / 8);
		
		return x.operator_multipl(max).operator_plus(1.0);
	}
}
