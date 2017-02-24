package org.distributedea.problems.continuousoptimization.point.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;

public class ToolReadSolutionCO {

	public static IndividualPoint readSolution(File fileOfSolution,
			IAgentLogger logger) {
		
		IndividualEvaluated indiv = null;
		
		try {
			indiv =  IndividualEvaluated.importXML(fileOfSolution);
		} catch (Exception e) {
			logger.logThrowable("Can not read solution", e);
			return null;
		}
		
		return (IndividualPoint) indiv.getIndividual();
	}
}
