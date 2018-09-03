package org.distributedea.problemtools.tsp.gps.permutation.tools;

import java.util.List;
import java.util.logging.Level;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSP;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionGPS;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolFitnessTSPGPS {

	public static double evaluate(IndividualPermutation individual,
			DatasetTSP problem, IAgentLogger logger) {
		
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

			double distI = distanceBetween(possitionIstart, possitionIend, logger);
			if (distI < 0) {
				logger.log(Level.SEVERE, "Distance between two points is less zero");
			}
			distance += distI;
		}
		
	    return distance;
	}

	private static double distanceBetween(Position position1,
			Position position2, IAgentLogger logger)  {
		
		PositionGPS positionGPS1 = (PositionGPS) position1;
		PositionGPS positionGPS2 = (PositionGPS) position2;
		
		return ToolDistanceTSPGPS.distanceBetween(positionGPS1, positionGPS2, logger);
	}
}
