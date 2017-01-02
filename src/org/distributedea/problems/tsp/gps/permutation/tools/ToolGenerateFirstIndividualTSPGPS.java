package org.distributedea.problems.tsp.gps.permutation.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSP;

public class ToolGenerateFirstIndividualTSPGPS {

	public static IndividualPermutation generateFirstIndividual(ProblemTSP problemTSP,
			IAgentLogger logger) {
		
		int minValue = problemTSP.minOfPositionNumbers();
		
		List<Integer> permutation = new ArrayList<Integer>();
		for (int numberI = 0; numberI <  problemTSP.numberOfPositions();
				numberI++) {
			permutation.add(minValue + numberI);
		}
		
		return new IndividualPermutation(permutation);
	}
}
