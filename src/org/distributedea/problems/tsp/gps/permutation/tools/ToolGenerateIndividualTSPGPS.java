package org.distributedea.problems.tsp.gps.permutation.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.jgap.impl.StockRandomGenerator;


public class ToolGenerateIndividualTSPGPS {

	public static Individual generateIndividual(ProblemTSPGPS problemTSP,
			IAgentLogger logger) {
		
		List<Position> positions = new ArrayList<Position>();
		for (PositionGPS positionI : problemTSP.getPositions()) {
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
