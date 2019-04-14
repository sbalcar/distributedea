package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f14 implements IFuncitonCO {

	double[][] matrixR;
	
	@Override
	public void initialisation(int d) {
		
		matrixR = BbobOperations.getRandomRotationMatrix(d);
	}

	@Override
	public double evaluate(IndividualPoint x) {

		int d = x.getCoordinates().size();
		
		double[] z = BbobOperations.multipl(matrixR, x.exortAsArray());
		
		double sum = 0;
		for (int i = 0; i < d; i++) {
			
			double exponent = 2 + 4.0* i / (d -1);
			sum += Math.pow(Math.abs(z[i]), exponent);
		}
		
		return Math.sqrt(sum);
	}

}
