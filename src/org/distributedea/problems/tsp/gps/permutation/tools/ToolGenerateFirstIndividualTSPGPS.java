package org.distributedea.problems.tsp.gps.permutation.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSP;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolGenerateFirstIndividualTSPGPS {

	public static IndividualPermutation generateFirstIndividual(DatasetTSPGPS datasetTSP,
			IAgentLogger logger) {
		return ToolGenerateIndividualTSPGPS.generateIndividual(datasetTSP, logger);
	}
	
	@SuppressWarnings("unused")
	private  static IndividualPermutation generateFirstIndividual_(DatasetTSP problemTSP,
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
