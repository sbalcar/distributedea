package org.distributedea.problemtools.matrixfactorization.latentfactor.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;

public class ToolReadSolutionMF {

	public static IndividualLatentFactors readSolution(File fileOfSolution,
			IAgentLogger logger) {
		
		IndividualEvaluated indiv = null;
		
		try {
			indiv =  IndividualEvaluated.importXML(fileOfSolution);
		} catch (Exception e) {
			logger.logThrowable("Can not read solution", e);
			return null;
		}
		
		return (IndividualLatentFactors) indiv.getIndividual();
	}
}
