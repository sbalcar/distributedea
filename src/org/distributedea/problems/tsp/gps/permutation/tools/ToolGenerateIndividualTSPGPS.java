package org.distributedea.problems.tsp.gps.permutation.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionGPS;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.jgap.impl.StockRandomGenerator;


public class ToolGenerateIndividualTSPGPS {

	public static IndividualPermutation generateIndividual(DatasetTSPGPS datasetTSP,
			IAgentLogger logger) {
		
		List<Position> positions = new ArrayList<Position>();
		for (PositionGPS positionI : datasetTSP.getPositions()) {
			positions.add(positionI);
		}
		return generateIndividual(positions);
	}
	
	private static IndividualPermutation generateIndividual(List<Position> positions) {
		
		List<Integer> numbers = new ArrayList<>();
		for (Position positionI : positions) {
			numbers.add(positionI.getNumber());
		}
		
		Random rn = new StockRandomGenerator();
		List<Integer> permutation = new ArrayList<>();
		
		while (! numbers.isEmpty()) {
		
			int rndIndex = rn.nextInt(numbers.size());
			Integer valueI = numbers.remove(rndIndex);
			permutation.add(valueI);
		}
		
		return new IndividualPermutation(permutation);
	}
}
