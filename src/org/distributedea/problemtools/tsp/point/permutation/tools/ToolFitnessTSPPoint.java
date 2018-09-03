package org.distributedea.problemtools.tsp.point.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSP;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolFitnessTSPGPS;

public class ToolFitnessTSPPoint {

	public static double fitness(IndividualPermutation individual, DatasetTSP problem,
			IAgentLogger logger) {
		
		return ToolFitnessTSPGPS.evaluate(individual, problem, logger);
	}
}
