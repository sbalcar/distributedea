package org.distributedea.agents.computingagents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.ontology.problem.tsp.PositionGPS;

public abstract class Agent_TSP extends Agent_ComputingAgent {

	private static final long serialVersionUID = 1L;

	
	/**
	 * Generates the new Permutation based Individual from TSP-Problem
	 * @param problem
	 * @return
	 */
	protected IndividualPermutation generateIndividual(ProblemTSP problem) {
		
		List<Integer> numbers = new ArrayList<>();
		for (PositionGPS positionI : problem.getPositions()) {
			numbers.add(positionI.getNumber());
		}
		
		Random rn = new Random();
		List<Integer> permutation = new ArrayList<>();
		
		while (! numbers.isEmpty()) {
		
			int rndIndex = rn.nextInt(numbers.size());
			int valueI = numbers.remove(rndIndex);
			permutation.add(valueI);
		}
		
		IndividualPermutation individual = new IndividualPermutation();
		individual.setPermutation(permutation);
		
		return individual;
	}

	/**
	 * Mutation - swaps two genes
	 * @param individual
	 * @return
	 */
	protected IndividualPermutation mutation(IndividualPermutation individual) {
		
		List<Integer> permutation = individual.getPermutation();
		
		// copy
		List<Integer> permutationNew = new ArrayList<Integer>();
		for (int numberI : permutation) {
			permutationNew.add(numberI);
		}
		
		Random rn = new Random();
		int rndFirstIndex = rn.nextInt(permutationNew.size());
		int rndSecondIndex = rn.nextInt(permutationNew.size());
		
		int firstVaLue = permutationNew.get(rndFirstIndex);
		int secondVaLue = permutationNew.get(rndSecondIndex);
		
		permutationNew.set(rndFirstIndex, secondVaLue);
		permutationNew.set(rndSecondIndex, firstVaLue);
		
		IndividualPermutation individualNew = new IndividualPermutation();
		individualNew.setPermutation(permutationNew);
		
		return individualNew;
	}
	
	/**
	 * Counts fitness of the Permutation-Individual represents TSP-Problem
	 * 
	 * @param individual
	 * @param problem
	 * @return
	 */
	public double fitness(IndividualPermutation individual, ProblemTSP problem) {
		
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
			
			PositionGPS possitionIstart =
					problem.exportPositionGPS(itemNumberIstart);
			PositionGPS possitionIend =
					problem.exportPositionGPS(itemNumberIend);

			distance += distanceBetween(possitionIstart, possitionIend);
		}
		
	    return distance;
	}
	
	/**
	 * Converts degrees to radians
	 * 
	 * @param angle
	 * @return
	 */
	private double convertDegToRad(double angle) {
	    return angle * Math.PI / 180.0;
	}
	
	/**
	 * Counts distance between two points on the earth
	 * 
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return
	 */
	private double distanceBetween(double latitude1, double longitude1,
	                         double latitude2, double longitude2)  {
		
		if (latitude1 == latitude2 &&
				longitude1 == longitude2) {
			return 0;
		}
			
		double latitudeInRad1 = convertDegToRad(latitude1);
		double longitudeInRad1 = convertDegToRad(longitude1);
		double latitudeInRad2 = convertDegToRad(latitude2);
		double longitudeInRad2 = convertDegToRad(longitude2);
	 
	    double value = 6371 * Math.acos(
	        Math.sin(latitudeInRad1) * Math.sin(latitudeInRad2)
	        + Math.cos(latitudeInRad1) * Math.cos(latitudeInRad2)
	        * Math.cos(longitudeInRad2 - longitudeInRad1));
	    
	    if (Double.isNaN(value)) {
	    	logInfo("Error - Distance\n" +
	    			" latitudeInRad1" + latitudeInRad1 + "\n" +
	    			" latitudeInRad2" + latitudeInRad2 + "\n" +
	    			" longitudeInRad1" + longitudeInRad1 + "\n" +
	    			"longitudeInRad2" + longitudeInRad2);
	    }
	    
	    return value;
	}

	
	/**
	 * Counts distance between two GPS positions
	 * 
	 * @param position1
	 * @param position2
	 * @return
	 */
	public double distanceBetween(PositionGPS position1,
			PositionGPS position2)  {
		
		return distanceBetween(
				position1.getLatitude(),
				position1.getLongitude(),
				position2.getLatitude(),
				position2.getLongitude()
				);
	}
}
