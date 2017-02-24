package org.distributedea.problems.machinelearning.arguments.operators;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.argumentsdefinition.ArgumentDef;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;

public class OperatorDifferential {

	public static IndividualArguments create(IndividualArguments individualArgs1,
			IndividualArguments individualArgs2, IndividualArguments individualArgs3,
			double differentialWeightF, ProblemMachineLearning problemML,
			DatasetML datasetML, IAgentLogger logger) throws Exception {
		
		ArgumentsDef argumentsDef = problemML.getArgumentsDef();

		Arguments arguments1 = individualArgs1.getArguments();
		Arguments arguments2 = individualArgs2.getArguments();
		Arguments arguments3 = individualArgs3.getArguments();
		
		Arguments argumentsNew = new Arguments();
		for (ArgumentDef argumentDefI : argumentsDef.getArgumentsDef()) {
			
			String argumentName = argumentDefI.getName();
			
			Argument argument1 = arguments1.exportArgument(argumentName);
			Argument argument2 = arguments2.exportArgument(argumentName);
			Argument argument3 = arguments3.exportArgument(argumentName);
			
			// result = argument1 + F * (argument2 - argument3) 
			Argument difference = argumentDefI.exportDifferenceValue(argument2, argument3);
			Argument product = argumentDefI.exportProductValue(difference, differentialWeightF);
			
			Argument sum = argumentDefI.exportSumValue(argument1, product);
			
			argumentsNew.addArgument(
					argumentDefI.exportCorrectedValue(sum));
		}
		
		return new IndividualArguments(argumentsNew);
	}
}
