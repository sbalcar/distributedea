package org.distributedea.tests.continousoptimalization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.distributedea.agents.computingagents.Agent_EvolutionJGAP;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobException;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;
import org.distributedea.problems.continuousoptimization.bbobv1502.JNIfgeneric;
import org.distributedea.problems.continuousoptimization.point.ProblemToolBruteForceCO;


public class TestCO {

	@SuppressWarnings("unused")
	private void test1() throws Exception {
		
		String inputFileName = "inputs" + File.separator + "f01.co";
		IDatasetDescription datasetDescr =
				new DatasetDescription(new File(inputFileName));
		
		IProblem problem = new ProblemContinuousOpt("f01", 2, false);
		IProblemTool tool = new ProblemToolBruteForceCO(0.005);
		
		Dataset dataset = tool.readDataset(datasetDescr, problem, null);
		tool.initialization(problem, dataset, null, null, null);
		
		IndividualEvaluated individual = tool.generateIndividualEval(problem, dataset, null, null);
		
		double fitness = tool.fitness(individual.getIndividual(), problem, dataset, null);
		System.out.println(fitness);
		
		tool.exit();
	}
	
	@SuppressWarnings("unused")
	private void test2() throws Exception {
		
		String inputFileName = "inputs" + File.separator + "f01.co";
		IDatasetDescription datasetDescr =
				new DatasetDescription(new File(inputFileName));
		
		IProblemTool tool1 = new ProblemToolBruteForceCO(0.005);
		
		IProblem problem = new ProblemContinuousOpt("f01", 2, false);
		Dataset dataset1 = tool1.readDataset(datasetDescr, problem, null);
		tool1.initialization(problem, dataset1, null, null, null);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		IProblemTool tool2 = new ProblemToolBruteForceCO(0.005);
		
		Dataset dataset2 = tool2.readDataset(datasetDescr, problem, null);
		tool2.initialization(problem, dataset2, null, null, null);
		
		
		for (int i = 0; i < 10; i++) {
			IndividualEvaluated individualEval1 = tool1.generateIndividualEval(problem, dataset1, null, null);
			Individual individual1 = individualEval1.getIndividual();
			
			double fitness1 = tool1.fitness(individualEval1.getIndividual(), problem, dataset1, null);
			System.out.println(" fitness1: " + fitness1);		
	
			
			IndividualEvaluated individualEval2 = tool2.generateIndividualEval(problem, dataset2, null, null);
			Individual individual2 = individualEval2.getIndividual();
			
			double fitness2 = tool2.fitness(individual2, problem, dataset2, null);
			System.out.println(" fitness2: " + fitness2);
		}
		
		
		tool1.exit();
				
		tool2.exit();
	}

	@SuppressWarnings("unused")
	private void test3() throws BbobException, Exception {
	    
	    String inputFileName = "inputs" + File.separator + "f01.co";
		IDatasetDescription datasetDescr =
				new DatasetDescription(new File(inputFileName));
	    
		IProblem problem = new ProblemContinuousOpt("f01", 2, false);
		
	    IProblemTool tool = new ProblemToolBruteForceCO(0.005);
	    Dataset dataset = tool.readDataset(datasetDescr, problem, null);
		tool.initialization(problem, dataset, null, null, null);
		
		IndividualEvaluated individualEval1 = tool.generateIndividualEval(problem, dataset, null, null);
		Individual individual1 = individualEval1.getIndividual();
		
		IndividualPoint individualPoint1 = (IndividualPoint) individual1;
		List<Double> coordinatesList1 = individualPoint1.getCoordinates();

		Double[] coordinatesArray1 = new Double[coordinatesList1.size()];
		coordinatesList1.toArray(coordinatesArray1);
		
		double[] coordinates1 = ArrayUtils.toPrimitive(coordinatesArray1);
		
		
		IndividualEvaluated individualEval2 = tool.generateIndividualEval(problem, dataset, null, null);
		Individual individual2 = individualEval2.getIndividual();
		
		IndividualPoint individualPoint2 = (IndividualPoint) individual2;
		List<Double> coordinatesList2 = individualPoint2.getCoordinates();

		Double[] coordinatesArray2 = new Double[coordinatesList2.size()];
		coordinatesList2.toArray(coordinatesArray2);
		
		double[] coordinates2 = ArrayUtils.toPrimitive(coordinatesArray2);
		
		int number1 = 1;
		
		BbobTools bbobTool1 = new BbobTools("1", null);
		IJNIfgeneric fgeneric1 = bbobTool1.getInstanceJNIfgeneric();

	    JNIfgeneric.Params params1 = new JNIfgeneric.Params();
	    fgeneric1.initBBOB(1, number1, 2, "log/bbob/data1", params1);
	    
	    double val1 = fgeneric1.evaluate(coordinates1);
	    System.out.println("Fitness1:" + val1);
		
		
		int number2 = 2;
		
		BbobTools bbobTool2 = new BbobTools("1", null);
		IJNIfgeneric fgeneric2 = bbobTool2.getInstanceJNIfgeneric();

	    JNIfgeneric.Params params2 = new JNIfgeneric.Params();
	    fgeneric2.initBBOB(1, number2, 2, "log/bbob/data2", params2);
	    
	    double val2 = fgeneric2.evaluate(coordinates2);
	    System.out.println("Fitness2:" + val2);
	    
	    fgeneric1.exitBBOB();
	    bbobTool1.clean();
	    
	    fgeneric2.exitBBOB();
	    bbobTool2.clean();
	    
	}
	
	@SuppressWarnings("unused")
	private void test4() {
		
		String inputFileName = "inputs" + File.separator + "f01.co";
		IDatasetDescription datasetDescr =
				new DatasetDescription(new File(inputFileName));
		
		IProblem problem = new ProblemContinuousOpt("f01", 2, false);

		IProblemTool problemTool = new ProblemToolBruteForceCO(0.005);
		Dataset dataset = problemTool.readDataset(datasetDescr, problem, null);
		
		
		String solutionFileName = "log" + File.separator + "result" +
				File.separator + "Agent_Evolution_14.rslt";
		File fileOfSolution = new File(solutionFileName);
		
		IProblemTool tool = new ProblemToolBruteForceCO(0.005);
		
		Individual individual = tool.readSolution(fileOfSolution, dataset, null);
	}
	
	
	private void test5() {
		
		IProblem problem = new ProblemContinuousOpt("f01", 2, false);
		
		Map<MethodDescription, Integer> map = new HashMap<MethodDescription, Integer>();
		
		AgentConfiguration ac1 = new AgentConfiguration("Agent_Evolution-17",
				Agent_EvolutionJGAP.class, new Arguments(new ArrayList<Argument>()));
		ProblemToolDefinition probToolDef1 =
				new ProblemToolDefinition(new ProblemToolBruteForceCO(0.005));
		MethodDescription a1 =
				new MethodDescription(ac1, new MethodIDs(1), problem, probToolDef1);
		
		AgentConfiguration ac2 = new AgentConfiguration("Agent_Evolution-17",
				Agent_EvolutionJGAP.class, new Arguments(new ArrayList<Argument>()));
		ProblemToolDefinition probToolDef2 =
				new ProblemToolDefinition(new ProblemToolBruteForceCO(0.005));
		MethodDescription a2 =
				new MethodDescription(ac2, new MethodIDs(2), problem, probToolDef2);
		
		map.put(a1, 1);
		map.put(a2, 2);
		
		// Agent description should be the same
		if (map.size() != 1) {
			System.out.println("Error");
		}
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(80);
		list.add(3);
		list.add(-4);
		list.add(8);
		list.add(55);
		java.util.Collections.sort(list);
		
		
	}
	
	public static void main(String [] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InterruptedException {

		TestCO test = new TestCO();
		//test.test1();
		//test.test2();
		//test.test3();
		//test.test4();
		test.test5();
		
	}
	
}
