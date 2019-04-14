package org.distributedea.tests.tsp;

import java.io.File;

import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.tsp.point.permutation.ProblemToolBruteForceTSPPoint;

public class TestTSP {

	public static void main(String [] args) {
			
		// Euclidean 2D distance
		IProblemTool problemToolGPS = new ProblemToolBruteForceTSPPoint();
		
		boolean fitnessGPSNaNResult = false;
		double fitnessGPSNaN = problemToolGPS.fitness(null, null, null, null);
		if (Double.isNaN(fitnessGPSNaN)) {
			fitnessGPSNaNResult = true;
		}
		
		String italyFileName = "it16862";
		double italyExpectedValue = 557315;
		boolean italyResult = genericTSPTest(italyFileName, problemToolGPS,
				italyExpectedValue, 0);

		String pbnFileName = "pbn423";
		double pbnExpectedValue = 1365;
		boolean pbnResult = genericTSPTest(pbnFileName, problemToolGPS,
				pbnExpectedValue, 0);
		
		boolean resultGPS_E = fitnessGPSNaNResult && italyResult && pbnResult;
		
		
		IProblemTool problemToolPoint = new ProblemToolBruteForceTSPPoint();
		
		boolean fitnessPointNaNResult = false;
		double fitnessPointNaN = problemToolGPS.fitness(null, null, null, null);
		if (Double.isNaN(fitnessPointNaN)) {
			fitnessPointNaNResult = true;
		}
		
		String simpleTestName = "simpleTest";
		double simpleTestExpectedValue = 1025.509594617172;
		boolean simpleTestResult = genericTSPTest(simpleTestName, problemToolPoint,
				simpleTestExpectedValue, 1);
		
		String xqfFileName = "xqf131";
		double xqfExpectedValue = 564;
		boolean xqfResult = genericTSPTest(xqfFileName, problemToolPoint,
				xqfExpectedValue, 0);
		
		String xqlFileName = "xql662";
		double xqlExpectedValue = 2513;
		boolean xqlResult = genericTSPTest(xqlFileName, problemToolPoint,
				xqlExpectedValue, 0);
		
		String xitFileName = "xit1083";
		double xitExpectedValue = 3558;
		boolean xitResult = genericTSPTest(xitFileName, problemToolPoint,
				xitExpectedValue, 0);
		
		String pbmFileName = "pbm436";
		double pbmExpectedValue = 1443;
		boolean pbmResult = genericTSPTest(pbmFileName, problemToolPoint,
				pbmExpectedValue, 0);
		
		String djbFileName = "djb2036";
		double djbExpectedValue = 6197;
		boolean djbResult = genericTSPTest(djbFileName, problemToolPoint,
				djbExpectedValue, 0);
		
		String xmcFileName = "xmc10150";
		double xmcExpectedValue = 28387;
		boolean xmcResult = genericTSPTest(xmcFileName, problemToolPoint,
				xmcExpectedValue, 0);
		
		String fncFileName = "fnc19402";
		double fncExpectedValue = 59288;
		boolean fncResult = genericTSPTest(fncFileName, problemToolPoint,
				fncExpectedValue, 0);
		
		boolean resultPoint = fitnessPointNaNResult && 
				simpleTestResult &&  xqfResult && xqlResult && xitResult &&
				pbmResult && djbResult && xmcResult && fncResult;
		
		
		if (resultGPS_E && resultPoint) {
		
			System.out.println("Tests are OK");
		} else {
			System.out.println("Some Test is wrong");
		}
		
	}

	private static boolean genericTSPTest(String inputFileName,
			IProblemTool problemTool, double expectedFitness, double permissibleError) {
		
		AgentLogger logger = new AgentLogger(null);
		
		
		String problemFileName = FileNames.getDirectoryOfInputs() +
				File.separator + inputFileName + ".tsp";
		IDatasetDescription datasetDescr =
				new DatasetDescription(new File(problemFileName));
		
		Dataset dataset = problemTool.readDataset(datasetDescr, null, logger);
		
		ProblemWrapper problemWrapper = new ProblemWrapper();
		problemWrapper.setProblemToolDefinition(
				new ProblemToolDefinition(problemTool));

		ProblemTSPGPS problem = new ProblemTSPGPS();
		
		File fileOfSolution = new File(inputFileName + ".tour");
		
		Individual individual = (IndividualPermutation)
				problemTool.readSolution(fileOfSolution, null, logger);
		
		double value = problemTool.fitness(individual, problem, dataset, logger);
		System.out.println("Expected Fitness: " + expectedFitness);
		System.out.println("Fitness: " + value);
		
		if (Math.abs(expectedFitness - value) <= permissibleError) {
			return true;
		}
		
		return false;
	}
	
}
