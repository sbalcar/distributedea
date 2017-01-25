package org.distributedea.problems.binpacking.permutation.tools;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.dataset.binpacking.ObjectBinPack;
import org.distributedea.ontology.individuals.IndividualPermutation;

public class ToolFitnessBinPacking {

	public static double evaluate(IndividualPermutation individual,
			DatasetBinPacking problem, IAgentLogger logger) {
		
		if (individual == null) {
			return Double.NaN;
		}
		
		List<ObjectBinPack> objectsInOrderOfPermutation = new ArrayList<>();
		for (int numberOfPermI : individual.getPermutation()) {
			
			ObjectBinPack objectI =
					problem.exportObjectBinPackBy(numberOfPermI);
			
			objectsInOrderOfPermutation.add(objectI);
		}
		
		int numberOfBins = 0;
		double crowded = 0;
		
		for (ObjectBinPack objectI : objectsInOrderOfPermutation) {
			
			double sizeOfObjectI = objectI.getSize();
			
			if (crowded + sizeOfObjectI <= 1) {
				crowded += sizeOfObjectI;
			} else {
				numberOfBins++;
				crowded = sizeOfObjectI;
			}
		}
		
		return numberOfBins +1;
	}
	
}
