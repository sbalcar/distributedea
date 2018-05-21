package org.distributedea.problems.machinelearning.arguments.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.problem.IProblem;

public class ToolReadProblemML {

	public static DatasetML readProblem(File problemFile, IProblem problem,
			IAgentLogger logger) {
		
		return new DatasetML(problemFile);
	}
}
