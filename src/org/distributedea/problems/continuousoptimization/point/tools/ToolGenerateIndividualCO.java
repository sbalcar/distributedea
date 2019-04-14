package org.distributedea.problems.continuousoptimization.point.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.continuousoptimization.DomainDefinition;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;

public class ToolGenerateIndividualCO {

	public static Individual generateIndividual(int dimension,
			DomainDefinition domainDef, IAgentLogger logger) {
				
		IndividualPoint individual = new IndividualPoint();
		for (int i = 0; i < dimension; i++) {
			Interval intervalI = domainDef.exportRestriction(i);
			
			double min = intervalI.getMin();
			double max = intervalI.getMax();
			double dis = max -min;
			
			double val = min + Math.random() * dis;
			individual.importCoordinate(i, val);
		}
		
		return individual;
	}
}
