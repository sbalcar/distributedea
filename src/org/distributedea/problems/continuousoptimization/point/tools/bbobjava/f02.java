package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f02 implements IFuncitonCO {

	@Override
	public void initialisation(int d) {
	}
	
	@Override
	public double evaluate(IndividualPoint x) {
	
		int d = x.getCoordinates().size();
		
		double[] z = x.exortAsArray();
		
		double result = 0;
		for (int i = 0; i < d; i++) {
			
			double exponent = 6.0*i/(d-1);	
			result += Math.pow(10, exponent) * Math.pow(z[i], 2);
		}
		
		return result;
	}

}
