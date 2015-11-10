package org.distributedea.tests.tsp;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2DSimpleSwap;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPointSimpleSwap;

public class TestTSP {

	public static void main(String [] args) {
			
		// Euclidean 2D distance
		ProblemTool problemToolGPS = new ProblemToolGPSEuc2DSimpleSwap();
		
		String italyFileName = "it16862";
		double italyExpectedValue = 557315;
		boolean italyResult = genericTSPTest(italyFileName, problemToolGPS,
				italyExpectedValue);

		String pbnFileName = "pbn423";
		double pbnExpectedValue = 1365;
		boolean pbnResult = genericTSPTest(pbnFileName, problemToolGPS,
				pbnExpectedValue);
		
		boolean resultGPS_E = italyResult && pbnResult;
		
		
		ProblemTool problemToolPoint = new ProblemToolPointSimpleSwap();
		
		String xqfFileName = "xqf131";
		double xqfExpectedValue = 564;
		boolean xqfResult = genericTSPTest(xqfFileName, problemToolPoint,
				xqfExpectedValue);
		
		String xqlFileName = "xql662";
		double xqlExpectedValue = 2513;
		boolean xqlResult = genericTSPTest(xqlFileName, problemToolPoint,
				xqlExpectedValue);
		
		String xitFileName = "xit1083";
		double xitExpectedValue = 3558;
		boolean xitResult = genericTSPTest(xitFileName, problemToolPoint,
				xitExpectedValue);
		
		String pbmFileName = "pbm436";
		double pbmExpectedValue = 1443;
		boolean pbmResult = genericTSPTest(pbmFileName, problemToolPoint,
				pbmExpectedValue);
		
		String djbFileName = "djb2036";
		double djbExpectedValue = 6197;
		boolean djbResult = genericTSPTest(djbFileName, problemToolPoint,
				djbExpectedValue);
		
		String xmcFileName = "xmc10150";
		double xmcExpectedValue = 28387;
		boolean xmcResult = genericTSPTest(xmcFileName, problemToolPoint,
				xmcExpectedValue);
		
		String fncFileName = "fnc19402";
		double fncExpectedValue = 59288;
		boolean fncResult = genericTSPTest(fncFileName, problemToolPoint,
				fncExpectedValue);
		
		boolean resultPoint = xqfResult && xqlResult && xitResult &&
				pbmResult && djbResult && xmcResult && fncResult;
		
		
		if (resultGPS_E && resultPoint) {
		
			System.out.println("Tests are OK");
		} else {
			System.out.println("Some Test is wrong");
		}
		
	}

	private static boolean genericTSPTest(String inputFileName,
			ProblemTool problemTool, double expectedFitness) {
		
		AgentLogger logger = new AgentLogger(null);
		
		String problemFileName = 
				org.distributedea.Configuration.getInputFile(inputFileName + ".tsp");
		
		Problem problem = problemTool.readProblem(problemFileName, logger);
		problem.setProblemToolClass(problemTool.getClass().getName());
		
		
		Individual individual = (IndividualPermutation)
				problemTool.readSolution(inputFileName + ".tour", null, logger);
		
		double value = problemTool.fitness(individual, problem, logger);
		System.out.println("Expected Fitness: " + expectedFitness);
		System.out.println("Fitness: " + value);
		
		if (expectedFitness == value) {
			return true;
		}
		
		return false;
	}
	
}
