package org.distributedea.tests.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.distributedea.problems.tsp.permutation.ProblemToolSimpleSwap;

public class TestGPS {

	public static void main(String [] args) {
		
		if (	test1() &&
				test2()
				) {
		
			System.out.println("Test is OK");
		}
		
	}

	private static boolean test1() {
		
		PositionGPS gpsLondon = new PositionGPS();
		gpsLondon.setNumber(1);
		gpsLondon.setLatitude(51.50735);
		gpsLondon.setLongitude(-0.12775);
		
		PositionGPS gpsParis = new PositionGPS();
		gpsParis.setNumber(2);
		gpsParis.setLatitude(48.85661);
		gpsParis.setLongitude(2.35222);

		PositionGPS gpsRoma = new PositionGPS();
		gpsRoma.setNumber(3);
		gpsRoma.setLatitude(41.90278);
		gpsRoma.setLongitude(12.49636);

		PositionGPS gpsCairo = new PositionGPS();
		gpsCairo.setNumber(4);
		gpsCairo.setLatitude(30.04441);
		gpsCairo.setLongitude(31.23571);

		PositionGPS gpsHongKong = new PositionGPS();
		gpsHongKong.setNumber(5);
		gpsHongKong.setLatitude(22.27831);
		gpsHongKong.setLongitude(114.17469);
		
		PositionGPS gpsMoscow = new PositionGPS();
		gpsMoscow.setNumber(6);
		gpsMoscow.setLatitude(55.75582);
		gpsMoscow.setLongitude(37.6173);

		PositionGPS[] positionsArray = {gpsLondon, gpsParis, gpsRoma, gpsCairo, gpsHongKong, gpsMoscow};
		List<PositionGPS> positions = new ArrayList<PositionGPS>(Arrays.asList(positionsArray));

		ProblemTSP problem = new ProblemTSP();
		problem.setPositions(positions);

		
		double distance = 0;
		
		ProblemToolSimpleSwap problemTool = new ProblemToolSimpleSwap();
		
		double kmLondonParis = problemTool.distanceBetween(gpsLondon, gpsParis, null);
		System.out.println("London-Paris km: " + kmLondonParis);
		distance += kmLondonParis;
		
		double kmParisRoma = problemTool.distanceBetween(gpsParis, gpsRoma, null);
		System.out.println("Paris-Roma km: " + kmParisRoma);
		distance += kmParisRoma;

		double kmRomaCairo = problemTool.distanceBetween(gpsRoma, gpsCairo, null);
		System.out.println("Roma-Cairo km: " + kmRomaCairo);
		distance += kmRomaCairo;
		
		double kmCairoHongKong = problemTool.distanceBetween(gpsCairo, gpsHongKong, null);
		System.out.println("Cairo-HongKong km: " + kmCairoHongKong);
		distance += kmCairoHongKong;
		
		double kmHongKongMoscow = problemTool.distanceBetween(gpsHongKong, gpsMoscow, null);
		System.out.println("HongKong-Moscow km: " + kmHongKongMoscow);
		distance += kmHongKongMoscow;
		
		double kmMoscowLondon = problemTool.distanceBetween(gpsMoscow, gpsLondon, null);
		System.out.println("Moscow-London km: " + kmMoscowLondon);
		distance += kmMoscowLondon;
		
		
		System.out.println("Total distance km: " + distance);
		
		Integer[] permutation = {1, 2, 3, 4, 5, 6};
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(permutation));
		
		IndividualPermutation individual = new IndividualPermutation();
		individual.setPermutation(list);
		
		double fitness = problemTool.fitness(individual, problem, null);
		System.out.println("Fitness km: " + fitness);
		
		if (distance == fitness &&
				20*1000 < distance && distance < 25*1000) {
			return true;
		}
		
		return false;
	}
	
	private static boolean test2() {
		
		String inputFileName = "it16862";
		
		ProblemToolSimpleSwap problemTool = new ProblemToolSimpleSwap();
		
		AgentLogger logger = new AgentLogger(null);
		
		
		String problemFileName = 
				org.distributedea.Configuration.getInputFile(inputFileName + ".tsp");
		
		Problem problem = problemTool.readProblem(problemFileName, logger);
		problem.setProblemToolClass(problemTool.getClass().getName());
		
		
		Individual individual = (IndividualPermutation)
				problemTool.readSolution(inputFileName + ".tour", null, logger);
		
		double value = problemTool.fitness(individual, problem, logger);
		System.out.println("Fitness: " + value);
		
		return true;
	}
	
}
