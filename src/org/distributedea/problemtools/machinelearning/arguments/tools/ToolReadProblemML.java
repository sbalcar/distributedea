package org.distributedea.problemtools.machinelearning.arguments.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.problem.IProblem;

public class ToolReadProblemML {

	public static DatasetML readProblem(DatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {
		
		return new DatasetML(datasetDescription.exportDatasetFile());
	}
}
