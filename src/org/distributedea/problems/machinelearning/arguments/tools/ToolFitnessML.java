package org.distributedea.problems.machinelearning.arguments.tools;

import java.util.List;
import java.util.ArrayList;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.argumentsdefinition.ArgumentDef;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefSwitch;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.machinelearning.arguments.tools.weka.MLWekaClassification;

public class ToolFitnessML {

	public static double evaluate(IndividualArguments individual,
			ProblemMachineLearning problem, DatasetML dataset,
			IAgentLogger logger) throws Exception {
		
		if (individual == null) {
			return Double.NaN;
		}
		
		Arguments arguments = individual.getArguments();
		ArgumentsDef argumentsDef = problem.getArgumentsDef();
		
		List<String> argumentsWeka = new ArrayList<>();
		for (Argument argI : arguments.getArguments()) {
			
			String argNameI = argI.getName();
			
			ArgumentDef argDefI = argumentsDef.exportArgumentsDef(argNameI);
			
			
			if (argDefI instanceof ArgumentDefSwitch) {
				if (argI.exportValueAsBoolean()) {
					argumentsWeka.add("-" + argNameI + "");
				}
				continue;
			}
			
			String name = "-" + argNameI;
			String value = argI.getValue();
			
			argumentsWeka.add(name);
			if (! value.equals("")) {
				argumentsWeka.add(value);
			}
		}
	    
		List<String> filterOptions = new ArrayList<>();

		// run
		MLWekaClassification demo = new MLWekaClassification();
	    demo.setClassifier(
	    	problem.exportClassifierClass(), 
	    	argumentsWeka.toArray(new String[argumentsWeka.size()]) );
	    demo.setFilter(
	    	problem.exportFilterClass(),
	        filterOptions.toArray(new String[filterOptions.size()]) );
	    demo.setTraining(dataset.exportDatasetFile());
	    demo.execute();
	    
	    //System.out.println("ErrorRate: " + demo.getErrorRate());
	    //System.out.println(demo.toString());
	    
	    double errorRate = demo.getErrorRate();
	    
	    //System.out.println("Fitness: " + errorRate + " " + argumentsWeka);
	    
		return errorRate;
	}
}
