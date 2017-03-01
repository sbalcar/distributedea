package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f04 {

	public static double evaluate(IndividualPoint x) {
		
		int d = x.getCoordinates().size();
		
		IndividualPoint z = countZ(x, d);
		
		double sumCos = 0;
		for (int i = 0; i < d; i++) {
			sumCos += Math.cos(2*Math.PI*z.exportCoordinate(i));
		}
		
		double sumz2 = 0;
		for (int i = 0; i < d; i++) {
			sumz2 += Math.pow(z.exportCoordinate(i), 2);
		}
		
		return 10*(d -sumCos) + sumz2 + 100*fpen(x, d);
	}
	
	private static IndividualPoint countZ(IndividualPoint x, int d) {
				
		List<Double> coordinatesZ = new ArrayList<>();
				
		for (int i = 0; i < d; i++) {
			
			double xi = x.exportCoordinate(i);
			
			double zi = countsi(i, xi, d) * countTosz(i, xi, d);
			coordinatesZ.add(zi);
		}

		return new IndividualPoint(coordinatesZ);
	}

	private static double countsi(int i, double xi, int d) {
		
		double result = 1;
		if (i % 2 == 0 && xi > 0) {
			result *= 10;
		}
		
		return result * Math.pow(Math.sqrt(10), i/(d-1));
	}

	private static double countTosz(int i, double xi, int d) {
		
		double xistr = 0;
		if (xi != 0) {
			xistr = Math.log(Math.abs(xi));
		}
		
		double c1;
		if (xi > 0) {
			c1 = 10.0;
		} else {
			c1 = 5.5;
		}

		double c2;
		if (xi > 0) {
			c2 = 7.9;
		} else {
			c2 = 3.1;
		}
		
		return Math.signum(xi) * Math.exp(xistr +0.049*(Math.sin(c1*xistr) + Math.sin(c2*xistr)));
	}
	
	private static double fpen(IndividualPoint x, int d) {
		
		double sum = 0;
		for (int i = 0; i < d; i++) {
			
			double xi = x.exportCoordinate(i);
			double max = Math.max(0, Math.abs(xi) -5);
			
			sum += Math.pow(max, 2);
		}
		
		return sum;
	}
}
