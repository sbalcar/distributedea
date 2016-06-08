package org.distributedea.tests.continousoptimalization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.continuousoptimization.ProblemToolRandomMove;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobException;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;
import org.distributedea.problems.continuousoptimization.bbobv1502.JNIfgeneric;
import org.distributedea.problems.exceptions.ProblemToolException;


public class TestCO {

	@SuppressWarnings("unused")
	private void test1() throws ProblemToolException {
		
		String inputFileName = "inputs" + File.separator + "f01.co";
		
		ProblemTool tool = new ProblemToolRandomMove();
		
		Problem problem = tool.readProblem(inputFileName, null);
		tool.initialization(problem, null, null);
		
		Individual individual = tool.generateIndividual(problem, null);
		
		double fitness = tool.fitness(individual, problem, null);
		System.out.println(fitness);
		
		tool.exit();
	}
	
	@SuppressWarnings("unused")
	private void test2() throws ProblemToolException {
		
		String inputFileName = "inputs" + File.separator + "f01.co";
		
		ProblemTool tool1 = new ProblemToolRandomMove();
		
		Problem problem1 = tool1.readProblem(inputFileName, null);
		tool1.initialization(problem1, null, null);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		ProblemTool tool2 = new ProblemToolRandomMove();
		
		Problem problem2 = tool2.readProblem(inputFileName, null);
		tool2.initialization(problem2, null, null);
		
		
		for (int i = 0; i < 10; i++) {
			Individual individual1 = tool1.generateIndividual(problem1, null);
			double fitness1 = tool1.fitness(individual1, problem1, null);
			System.out.println(" fitness1: " + fitness1);		
	
			Individual individual2 = tool2.generateIndividual(problem2, null);
			double fitness2 = tool2.fitness(individual2, problem2, null);
			System.out.println(" fitness2: " + fitness2);
		}
		
		
		tool1.exit();
				
		tool2.exit();
	}

	@SuppressWarnings("unused")
	private void test3() throws BbobException, ProblemToolException {
	    
	    String inputFileName = "inputs" + File.separator + "f01.co";
	    
	    ProblemTool tool = new ProblemToolRandomMove();
		Problem problem = tool.readProblem(inputFileName, null);
		tool.initialization(problem, null, null);
		
		Individual individual1 = tool.generateIndividual(problem, null);
		
		IndividualPoint individualPoint1 = (IndividualPoint) individual1;
		List<Double> coordinatesList1 = individualPoint1.getCoordinates();

		Double[] coordinatesArray1 = new Double[coordinatesList1.size()];
		coordinatesList1.toArray(coordinatesArray1);
		
		double[] coordinates1 = ArrayUtils.toPrimitive(coordinatesArray1);
		
		
		Individual individual2 = tool.generateIndividual(problem, null);
		
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
		
		ProblemTool problemTool = new ProblemToolRandomMove();
		Problem problem = problemTool.readProblem(inputFileName, null);
		
		
		String solutionFileName = "log" + File.separator + "result" +
				File.separator + "Agent_Evolution_14.rslt";
		
		ProblemToolRandomMove tool = new ProblemToolRandomMove();
		
		Individual individual = tool.readSolution(solutionFileName, problem, null);
	}
	
	
	private void test5() {
		
		Map<AgentDescription, Integer> map = new HashMap<AgentDescription, Integer>();
			
		AgentConfiguration ac1 = new AgentConfiguration();
		ac1.setAgentType(Agent_Evolution.class.getName());
		ac1.setAgentName("Agent_Evolution-17");
		AgentDescription a1 = new AgentDescription();
		a1.setAgentConfiguration(ac1);
		a1.setProblemToolClass(ProblemToolRandomMove.class.getName());
		
		AgentConfiguration ac2 = new AgentConfiguration();
		ac2.setAgentType(Agent_Evolution.class.getName());
		ac2.setAgentName("Agent_Evolution-17");
		AgentDescription a2 = new AgentDescription();
		a2.setAgentConfiguration(ac2);
		a2.setProblemToolClass(ProblemToolRandomMove.class.getName());
		
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
