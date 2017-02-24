package org.distributedea.tests.machinelearning;

import java.io.File;

import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefDouble;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefInteger;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.machinelearning.ProblemToolML;

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
	    
	    
	    ProblemToolML tool = new ProblemToolML();
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
	    
	    
	    ProblemToolML tool = new ProblemToolML();
	    double fitness = tool.fitness(individual, problem, datasetML, new TrashLogger());
	    System.out.println("Fitness: " + fitness);
	}

	public static void main(String [] args) throws Exception {
		
		test2();
		System.out.println("Test exit");
	}

}
