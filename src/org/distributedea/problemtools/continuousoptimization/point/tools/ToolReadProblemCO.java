package org.distributedea.problemtools.continuousoptimization.point.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.problem.IProblem;


public class ToolReadProblemCO {

	public static DatasetContinuousOpt readProblem(DatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {
		
		if (datasetDescription == null || ! datasetDescription.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		
		File file = datasetDescription.exportDatasetFile();

		Dataset dataset = null;
		try {
			dataset = DatasetContinuousOpt.importXML(file);
		} catch (Exception e) {
			return null;
		}
		
		return (DatasetContinuousOpt) dataset;
	}

}
