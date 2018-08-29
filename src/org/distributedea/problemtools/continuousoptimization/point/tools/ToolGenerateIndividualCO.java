package org.distributedea.problemtools.continuousoptimization.point.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.ProblemContinuousOpt;

public class ToolGenerateIndividualCO {

	public static Individual generateIndividual(ProblemContinuousOpt problemCo,
			DatasetContinuousOpt datasetCO, IAgentLogger logger) {
				
		IndividualPoint individual = new IndividualPoint();
		for (int i = 0; i < problemCo.getDimension(); i++) {
			Interval intervalI = datasetCO.getDomain().exportRestriction(i);
			
			double min = intervalI.getMin();
			double max = intervalI.getMax();
			double dis = max -min;
			
			double val = min + Math.random() * dis;
			individual.importCoordinate(i, val);
		}
		
		return individual;
	}
}
