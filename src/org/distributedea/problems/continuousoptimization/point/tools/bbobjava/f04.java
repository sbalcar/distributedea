package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public class f04 implements IFuncitonCO {

	@Override
	public void initialisation(int d) {
	}

	@Override
	public double evaluate(IndividualPoint indiv) {
		
		double[] x = indiv.exortAsArray();
		int d = x.length;
		
		double[] z = countZ(x);
		
		double sumCos = 0;
		for (int i = 0; i < d; i++) {
			sumCos += Math.cos(2*Math.PI*z[i]);
		}
		
		double sumz2 = 0;
		for (int i = 0; i < d; i++) {
			sumz2 += Math.pow(z[i], 2);
		}
		
		return 10*(d -sumCos) + sumz2 + 100*BbobOperations.fpen(x);
	}
	
	private double[] countZ(double[] x) {
		
		int d = x.length;
		
		double[] coordinatesZ = new double[d];
		
		for (int i = 0; i < d; i++) {
						
			coordinatesZ[i] = BbobOperations.countsi(i, x[i], d) *
					BbobOperations.countTosz(i, x[i], d);
		}

		return coordinatesZ;
	}

}
