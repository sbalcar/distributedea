package org.distributedea.problemtools.tsp.point.permutation.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionPoint;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.jgap.impl.StockRandomGenerator;

public class ToolGenerateIndividualForTSPPoint {

	public static IndividualPermutation generate(DatasetTSPPoint dataset) {
				
		List<Position> positions = new ArrayList<Position>();
		for (PositionPoint positionI : dataset.getPositions()) {
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
			int valueI = numbers.remove(rndIndex);
			permutation.add(valueI);
		}
		
		return new IndividualPermutation(permutation);
	}
}
