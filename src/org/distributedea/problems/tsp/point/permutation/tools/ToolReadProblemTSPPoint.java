package org.distributedea.problems.tsp.point.permutation.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolReadProblemTSPGPS;

public class ToolReadProblemTSPPoint {

	public static DatasetTSPGPS readDataset(File problemFile, IProblem problem, IAgentLogger logger) {
		
		return ToolReadProblemTSPGPS.readDataset(problemFile, problem, logger);
	}
}
