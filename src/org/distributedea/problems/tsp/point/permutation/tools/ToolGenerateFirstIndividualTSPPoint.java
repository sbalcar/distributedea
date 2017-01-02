package org.distributedea.problems.tsp.point.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolGenerateFirstIndividualTSPGPS;

public class ToolGenerateFirstIndividualTSPPoint {

	public IndividualPermutation generateFirst(ProblemTSPPoint problem,
			IAgentLogger logger) {
		
		return ToolGenerateFirstIndividualTSPGPS.generateFirstIndividual(problem, logger);
	}
}
