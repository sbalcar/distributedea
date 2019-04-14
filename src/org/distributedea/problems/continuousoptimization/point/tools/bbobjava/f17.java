package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f17 implements IFuncitonCO {

	double[][] matrixR;
	double[][] matrixQ;
	
	@Override
	public void initialisation(int d) {
		
		matrixR = BbobOperations.getRandomRotationMatrix(d);
		matrixQ = BbobOperations.getRandomRotationMatrix(d);
		
	}

	@Override
	public double evaluate(IndividualPoint x) {
		
		int d = x.getCoordinates().size();
		
		double[][] matrixDiag = BbobOperations.getDiagonalMatrix(10, d);
		
		double[] z =
				BbobOperations.multipl(matrixDiag,
					BbobOperations.multipl(matrixQ,						
						BbobOperations.countsTasy(0.5,
							BbobOperations.multipl(matrixR, x.exortAsArray()
				))));
		
		double sum = 0;
		for (int i = 0; i <= d -2; i++) {
			
			double si = Math.sqrt(z[i]*z[i] + z[i+1]*z[i+1]);
			
			double sinus = Math.sin(50 * Math.pow(50 * si, 0.2));
			sum += Math.sqrt(si) + Math.sqrt(si) * Math.pow(sinus, 2);
		}

		return Math.pow(1.0/(d-1)*sum, 2) + 10*BbobOperations.fpen(x.exortAsArray());
	}

}
