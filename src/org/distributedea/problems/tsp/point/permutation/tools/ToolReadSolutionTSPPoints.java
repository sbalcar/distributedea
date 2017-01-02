package org.distributedea.problems.tsp.point.permutation.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolReadSolutionTSPGPS;

public class ToolReadSolutionTSPPoints {

	public static IndividualPermutation readSolutionTSP(File fileOfSolution,
			IAgentLogger logger) {

		return ToolReadSolutionTSPGPS.readSolution(fileOfSolution, logger);
	}
}
