package org.distributedea.problems.tsp.point.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.problems.tsp.gps.permutation.IProblemTSPPermutationTool;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolFitnessTSPGPS;

public class ToolFitnessTSPPoint {

	public static double fitness(IndividualPermutation individual, ProblemTSP problem,
			IProblemTSPPermutationTool tool, IAgentLogger logger) {
		
		return ToolFitnessTSPGPS.evaluate(individual, problem, tool, logger);
	}
}
