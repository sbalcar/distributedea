package org.distributedea.tests.machinelearning;

import java.io.File;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefDouble;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefInteger;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefSwitch;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.machinelearning.ProblemToolMLRandomMove;

import weka.classifiers.functions.MultilayerPerceptron;

public class TestWeka {

	public static void test1() throws Exception {
		
		Class<?> classifier = weka.classifiers.trees.J48.class;
		Class<?> filter = weka.filters.unsupervised.instance.Randomize.class;
	    
		ProblemMachineLearning problem = new ProblemMachineLearning(classifier, filter, new ArgumentsDef());
		
		
	    String dataset = "inputs/iris.arff";
	    
	    DatasetML datasetML = new DatasetML(new File(dataset));


	    Argument arg = new Argument("U", "");
	    Arguments argss = new Arguments(arg);
	    
	    IndividualArguments individual = new IndividualArguments(argss);
	    
	    
	    ProblemToolMLRandomMove tool = new ProblemToolMLRandomMove();
	    tool.fitness(individual, problem, datasetML, new TrashLogger());

	}

	public static void test2() throws Exception {
		
		Class<?> classifier = MultilayerPerceptron.class;
		Class<?> filter = weka.filters.unsupervised.instance.Randomize.class;
	    
		ArgumentsDef argumentsDef = new ArgumentsDef();
		argumentsDef.addArgumentsDef(new ArgumentDefDouble("L", 0, 1)); // default 0.3
		argumentsDef.addArgumentsDef(new ArgumentDefDouble("M", 0, 1)); // default 0.2
		//argumentsDef.addArgumentsDef(new ArgumentDefInteger("N", 100, 500));  // default 500
		argumentsDef.addArgumentsDef(new ArgumentDefInteger("E", 20, 20));  // default 20
		
		ProblemMachineLearning problem = new ProblemMachineLearning(classifier, filter, argumentsDef);
		
		
	    //String dataset = "inputs/iris.arff";
	    //String dataset = "inputs/kdd_synthetic_control.arff";
	    String dataset = "inputs/zoo.arff";
	    
	    DatasetML datasetML = new DatasetML(new File(dataset));


	    Argument argL = new Argument("L", "0.9");
	    Argument argM = new Argument("M", "0.2");

	    Argument argN = new Argument("N", "100");
	    Argument argV = new Argument("V", "0");
	    
	    Argument argS = new Argument("S", "0");
	    Argument argE = new Argument("E", "20");
	    Argument argH = new Argument("H", "a");
	    
	    Arguments argss = new Arguments();
	    argss.addArgument(argL);
	    argss.addArgument(argM);
	    argss.addArgument(argN);
	    argss.addArgument(argV);
	    argss.addArgument(argS);
	    argss.addArgument(argE);
	    argss.addArgument(argH);
	    
	    IndividualArguments individual = new IndividualArguments(argumentsDef.exportRandomGeneratedArguments());
	    
	    
	    ProblemToolMLRandomMove tool = new ProblemToolMLRandomMove();
	    double fitness = tool.fitness(individual, problem, datasetML, new TrashLogger());
	    System.out.println("Fitness: " + fitness);
	}

	public static void test3() throws Exception {
		
		Class<?> classifier = weka.classifiers.trees.RandomForest.class;
		Class<?> filter = weka.filters.unsupervised.instance.Randomize.class;
		
		ArgumentsDef argumentsDef = new ArgumentsDef();
		argumentsDef.addArgumentsDef(new ArgumentDefInteger("P", 20, 100));  // default 100
		argumentsDef.addArgumentsDef(new ArgumentDefInteger("K", 1, 6));  // default 0
		//argumentsDef.addArgumentsDef(new ArgumentDefInteger("M", 1, 2));
		argumentsDef.addArgumentsDef(new ArgumentDefDouble("V", 0.0001, 0.5)); // default 0.003
		
		argumentsDef.addArgumentsDef(new ArgumentDefSwitch("U"));
		argumentsDef.addArgumentsDef(new ArgumentDefSwitch("B"));
		argumentsDef.addArgumentsDef(new ArgumentDefInteger("depth", 1, 20));
		argumentsDef.addArgumentsDef(new ArgumentDefInteger("I", 50, 300));  // default 100
		argumentsDef.addArgumentsDef(new ArgumentDefInteger("batch-size", 80, 120)); // default 100
	    
		ProblemMachineLearning problem = new ProblemMachineLearning(classifier, filter, argumentsDef);
		
		
	    String dataset = "inputs/wilt.arff";
	    
	    DatasetML datasetML = new DatasetML(new File(dataset));
/*
	    // P-82-K-5-V-0.34053986167585115-U-false-B-false-depth-7-I-187-batch-size-86-
	    Argument argP = new Argument("P", "82");
	    Argument argK = new Argument("K", "5");

	    Argument argV = new Argument("V", "0.34053986167585115");
	    Argument argU = new Argument("U", "false");
	    
	    Argument argB = new Argument("B", "false");
	    Argument argDe = new Argument("depth", "7");
	    Argument argI = new Argument("I", "187");
	    Argument argBa = new Argument("batch-size", "86");
	    
	    Arguments argss0 = new Arguments();
	    argss0.addArgument(argP);
	    argss0.addArgument(argK);
	    argss0.addArgument(argV);
	    argss0.addArgument(argU);
	    argss0.addArgument(argB);
	    argss0.addArgument(argDe);
	    argss0.addArgument(argI);
	    argss0.addArgument(argBa);
*/
	    
	    // P-56-K-5-V-0.2937554104227731-U-true-B-false-depth-19-I-30-batch-size-116-
	    Argument argP = new Argument("P", 56);
	    Argument argK = new Argument("K", 5);

	    Argument argV = new Argument("V", 0.2937554104227731);
	    Argument argU = new Argument("U", true);
	    
	    Argument argB = new Argument("B", false);
	    Argument argDe = new Argument("depth", 19);
	    Argument argI = new Argument("I", 30);
	    Argument argS = new Argument("S", 1);
	    Argument argBa = new Argument("batch-size", 116);
	    
	    Arguments argss0 = new Arguments();
	    argss0.addArgument(argP);
	    argss0.addArgument(argK);
	    argss0.addArgument(argV);
	    argss0.addArgument(argU);
	    argss0.addArgument(argB);
	    argss0.addArgument(argDe);
	    argss0.addArgument(argI);
	    argss0.addArgument(argS);
	    argss0.addArgument(argBa);
	    
	    
	    
	    //IndividualArguments individual = new IndividualArguments(argumentsDef.exportRandomGeneratedArguments());
	    IndividualArguments individual = new IndividualArguments(argss0);
	    
	    
	    ProblemToolMLRandomMove tool = new ProblemToolMLRandomMove();
	    double fitness = tool.fitness(individual, problem, datasetML, new TrashLogger());
	    System.out.println("Fitness: " + fitness);
	    System.out.println(individual.toLogString());
	}

	
	public static void main(String [] args) throws Exception {

		test3();
		System.out.println("Test exit");

	}

}
