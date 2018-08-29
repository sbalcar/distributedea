package org.distributedea.tests.machinelearning;

import java.io.File;
import java.util.Vector;

import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problemtools.machinelearning.arguments.tools.weka.MLWekaClassification;

public class TestDemoWeka {
	
	public static void main(String [] args) throws Exception {
		
		Class<?> classifier = weka.classifiers.trees.J48.class;
		Class<?> filter = weka.filters.unsupervised.instance.Randomize.class;
	    
		ProblemMachineLearning problem = new ProblemMachineLearning(classifier, filter, new ArgumentsDef());
		
	    String dataset = "inputs/iris.arff";
	    
	    DatasetML datasetML = new DatasetML(new File(dataset));
	    
	    Vector<String> classifierOptions = new Vector<>();
	    classifierOptions.add("-U");
	    Vector<String> filterOptions = new Vector<>();

		// run
		MLWekaClassification demo = new MLWekaClassification();
	    demo.setClassifier(
	    	problem.exportClassifierClass(), 
	        (String[]) classifierOptions.toArray(new String[classifierOptions.size()]));
	    demo.setFilter(
	    	problem.exportFilterClass(),
	        (String[]) filterOptions.toArray(new String[filterOptions.size()]));
	    demo.setTraining(datasetML.exportDatasetFile());
	    demo.execute();

	    System.out.println("ErrorRate: " + demo.getErrorRate());
	    System.out.println();
	    
	    System.out.println(demo.toString());

	}

}
