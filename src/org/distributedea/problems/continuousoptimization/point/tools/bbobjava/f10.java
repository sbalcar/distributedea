package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f10 implements IFuncitonCO {

	double[][] matrix;
	
	@Override
	public void initialisation(int d) {
		
		matrix = BbobOperations.getRandomRotationMatrix(d);
	}

	@Override
	public double evaluate(IndividualPoint x) {
		
		int d = x.getCoordinates().size();
		
		double[] z = BbobOperations.multipl(matrix, x.exortAsArray());
		
		double result = 0;
		for (int i = 0; i < d; i++) {
			
			double exponent = 6*i/(d-1);
			
			result += Math.pow(10, exponent) * Math.pow(z[i], 2);
		}
		
		return result;
	}

}
