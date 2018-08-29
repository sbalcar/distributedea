package org.distributedea.problemtools.machinelearning.arguments.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;

public class ToolReadSolutionML {

	public static IndividualArguments readSolution(File fileOfSolution,
			IAgentLogger logger) {
		
		IndividualEvaluated indiv = null;
		
		try {
			indiv = IndividualEvaluated.importXML(fileOfSolution);
		} catch (Exception e) {
			logger.logThrowable("Can not read solution", e);
			return null;
		}
		
		return (IndividualArguments)indiv.getIndividual();
	}
}
