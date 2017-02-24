package org.distributedea.problems.machinelearning.arguments.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetML;

public class ToolReadProblemML {

	public static DatasetML readProblem(File problemFile, IAgentLogger logger) {
		
		return new DatasetML(problemFile);
	}
}
