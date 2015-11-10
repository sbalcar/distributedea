package org.distributedea.problems.tsp.gps.permutation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.distributedea.problems.exceptions.ProblemToolException;
import org.distributedea.problems.tsp.gps.ProblemTSPGPSTool;
import org.jgap.impl.StockRandomGenerator;

/**
 * Abstract Problem Tool for TSP Problem for Permutation based representation
 * @author stepan
 *
 */
public abstract class ProblemTSPGPSEuc2DPermutationTool extends ProblemTSPGPSTool implements IProblemTSPPermutationTool {
	
	@Override
	public Class<?> reprezentationWhichUses() {
	
		return IndividualPermutation.class;
	}
	
	@Override
	public Individual generateIndividual(Problem problem,
			AgentLogger logger) {
		
		ProblemTSPGPS problemTSP = (ProblemTSPGPS) problem;
		return generateIndividual(problemTSP);
	}
	
	@Override
	public Individual readSolution(String fileName, Problem problem,
			AgentLogger logger) {

		String tspFileName = Configuration.getSolutionFile(fileName);
		return readSolutionTSP(tspFileName, logger);
	}
	
	@Override
	public double fitness(Individual individual, Problem problem,
			AgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		ProblemTSPGPS problemTSP = (ProblemTSPGPS) problem;
		
		return fitnessPermutation(individualPerm, problemTSP, this, logger);
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Problem problem, AgentLogger logger)
			throws ProblemToolException {

		throw new ProblemToolException("Not possible to implement in this context");
	}
	
	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			Individual individual4, Problem problem, AgentLogger logger)
			throws ProblemToolException {

		throw new ProblemToolException("Not possible to implement in this context");
	}
	
	
	/**
	 * Generates the new Permutation based Individual from TSP-Problem
	 * @param problem
	 * @return
	 */
	protected IndividualPermutation generateIndividual(ProblemTSPGPS problem) {
		
		List<Integer> numbers = new ArrayList<>();
		for (PositionGPS positionI : problem.getPositions()) {
			numbers.add(positionI.getNumber());
		}
		
		Random rn = new StockRandomGenerator();
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
	 * Reads TSP Permutation Solution
	 * @param tspFileName
	 * @param logger
	 * @return
	 */
	private IndividualPermutation readSolutionTSP(String tspFileName,
			AgentLogger logger) {
		
		List<Integer> permutation = new ArrayList<Integer>();
		
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(tspFileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.startsWith("NAME") ||
					sCurrentLine.startsWith("COMMENT") ||
					sCurrentLine.startsWith("TYPE") ||
					sCurrentLine.startsWith("DIMENSION") ||
					sCurrentLine.startsWith("TOUR_SECTION") ||
					sCurrentLine.startsWith("EOF") ) {
					
					logger.log(Level.INFO, sCurrentLine);

				} else {
					String delims = "[ ]+";
					String[] tokens = sCurrentLine.split(delims);
					
					int number = Integer.parseInt(tokens[0]);
					
					permutation.add(number);
				}
				
			}
 
		} catch (IOException exception) {
			logger.logThrowable("Problem with reading " + tspFileName + " file", exception);
			return null;
			
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				logger.logThrowable("Problem with closing the file: " + tspFileName, ex);
			}
		}
		
		//remove -1
		permutation.remove(permutation.size() -1);
		
		IndividualPermutation individual = new IndividualPermutation();
		individual.setPermutation(permutation);
		
		return individual;
	}
	
	
	/**
	 * Counts fitness of the Permutation-Individual represents TSP-Problem
	 * 
	 * @param individual
	 * @param problem
	 * @return
	 */
	public static double fitnessPermutation(IndividualPermutation individual,
			ProblemTSP problem, IProblemTSPPermutationTool tool,
			AgentLogger logger) {
		
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
	
	
	/**
	 * Counts distance between two GPS positions
	 * 
	 * @param position1
	 * @param position2
	 * @return
	 */
	public double distanceBetween(Position position1,
			Position position2, AgentLogger logger)  {
		
		PositionGPS positionGPS1 = (PositionGPS) position1;
		PositionGPS positionGPS2 = (PositionGPS) position2;
		
		return distanceBetween(
				positionGPS1.getLatitude(),
				positionGPS1.getLongitude(),
				positionGPS2.getLatitude(),
				positionGPS2.getLongitude(),
				logger
				);
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
			double latitude2, double longitude2, AgentLogger logger)  {
		
		if (latitude1 == latitude2 &&
				longitude1 == longitude2) {
			return 0;
		}	    
		
		double xd = (latitude1 - latitude2);
		double yd = (longitude1 - longitude2);

		return Math.round(Math.sqrt((xd * xd) + (yd * yd)));
	}
	
}
