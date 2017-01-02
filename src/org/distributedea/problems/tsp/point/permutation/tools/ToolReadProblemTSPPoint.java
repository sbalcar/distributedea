package org.distributedea.problems.tsp.point.permutation.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolReadProblemTSPGPS;

public class ToolReadProblemTSPPoint {

	public static ProblemTSPGPS readProblem(File problemFile, IAgentLogger logger) {
		
		return ToolReadProblemTSPGPS.readProblem(problemFile, logger);
	}
}
