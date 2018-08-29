package org.distributedea.problemtools.tsp.point.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolReadProblemTSPGPS;

public class ToolReadProblemTSPPoint {

	public static DatasetTSPGPS readDataset(DatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {
		
		return ToolReadProblemTSPGPS.readDataset(datasetDescription, problem, logger);
	}
}
