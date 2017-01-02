package org.distributedea.problems.tsp.point.permutation.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionPoint;
import org.jgap.impl.StockRandomGenerator;

public class ToolGenerateIndividualForTSPPoint {

	public static IndividualPermutation generate(ProblemTSPPoint problem) {
				
		List<Position> positions = new ArrayList<Position>();
		for (PositionPoint positionI : problem.getPositions()) {
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
