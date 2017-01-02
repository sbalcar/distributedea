package org.distributedea.problems.tsp.gps.permutation.tools;

import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.problems.tsp.gps.permutation.IProblemTSPPermutationTool;

public class ToolFitnessTSPGPS {

	public static double evaluate(IndividualPermutation individual,
			ProblemTSP problem, IProblemTSPPermutationTool tool,
			IAgentLogger logger) {
		
		if (individual == null) {
			return Double.NaN;
		}
		
		List<Integer> permutation = individual.getPermutation();
		
		double distance = 0;
		
		for (int permutationIndex = 0;
				permutationIndex < permutation.size(); permutationIndex++) {

			int itemNumberIstart = permutation.get(permutationIndex);
			int itemNumberIend;
			if (permutationIndex + 1 == permutation.size()) {
				itemNumberIend = permutation.get(0);
			} else {
				itemNumberIend = permutation.get(permutationIndex + 1);
			}
			
			Position possitionIstart =
					problem.exportPosition(itemNumberIstart);
			Position possitionIend =
					problem.exportPosition(itemNumberIend);

			double distI = tool.distanceBetween(possitionIstart, possitionIend, logger);
			if (distI < 0) {
				logger.log(Level.SEVERE, "Distance between two points is less zero");
			}
			distance += distI;
		}
		
	    return distance;
	}

}
