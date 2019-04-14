package org.distributedea.problems.continuousoptimization.point.tools.bbobjava;

import org.distributedea.ontology.individuals.IndividualPoint;

public interface IFuncitonCO {
	
	public void initialisation(int d);
	public double evaluate(IndividualPoint x);

}
