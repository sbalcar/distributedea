package org.distributedea.problems.binpacking.permutation.tools;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;

public class ToolReadSolutionBinPacking {

	public static IndividualPermutation readSolution(File fileOfSolution,
			IAgentLogger logger) {
		
		IndividualEvaluated indiv = null;
		
		try {
			indiv = IndividualEvaluated.importXML(fileOfSolution);
		} catch (Exception e) {
			logger.logThrowable("Can not read solution", e);
			return null;
		}
		
		return (IndividualPermutation) indiv.getIndividual();
	}
	
}
