package org.distributedea.problems.continuousoptimization.point.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;

public class ToolGenerateIndividualCO {

	public static Individual generateIndividual(DatasetContinuousOpt problemContinousOpt,
			IAgentLogger logger) {
				
		IndividualPoint individual = new IndividualPoint();
		for (int i = 0; i < problemContinousOpt.getIntervals().size(); i++) {
			Interval intervalI = problemContinousOpt.getIntervals().get(i);
			
			double min = intervalI.getMin();
			double max = intervalI.getMax();
			double dis = max -min;
			
			double val = min + Math.random() * dis;
			individual.importCoordinate(i, val);
		}
		
		return individual;
	}
}
