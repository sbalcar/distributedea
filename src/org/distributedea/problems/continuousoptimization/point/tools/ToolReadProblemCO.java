package org.distributedea.problems.continuousoptimization.point.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.problem.IProblem;


public class ToolReadProblemCO {

	public static DatasetContinuousOpt readProblem(File fileOfProblem,
			IProblem problem, IAgentLogger logger) {
		
		if (fileOfProblem == null || ! fileOfProblem.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		

		Dataset dataset = null;
		try {
			dataset = DatasetContinuousOpt.importXML(fileOfProblem);
		} catch (Exception e) {
			return null;
		}
		
		return (DatasetContinuousOpt) dataset;
	}

}
