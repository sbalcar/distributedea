package org.distributedea.problemtools.machinelearning.arguments.operators;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.argumentsdefinition.ArgumentDef;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;

public class OperatorMoveABitToTheRight {

	private static double maxStep = 0.005;
	
	public static Individual create(IndividualArguments individualArgs,
			ProblemMachineLearning problemML, IAgentLogger logger) {
		
		// definition of arguments and selection of the one argument definition
		ArgumentsDef argumentsDef = problemML.getArgumentsDef().
				exportsArgumentsWithoutRestrictionForFixedValue();

		Arguments arguments = individualArgs.getArguments();
		Arguments argumentsNew = arguments.deepClone();
		
		for (ArgumentDef argDefI : argumentsDef.getArgumentsDef()) {
			
			Argument argI = arguments.exportArgument(argDefI.getName());
			
			if (argDefI.exportIsTheLastValue(argI)) {
				argumentsNew.replaceArgument(
						argDefI.exportMinValue());
			} else {
				Argument argnextI = argDefI.exportNextValue(argI, maxStep);
				argumentsNew.replaceArgument(
						argDefI.exportCorrectedValue(argnextI));
				break;
			}
		}
		
		return new IndividualArguments(argumentsNew);
	}
}
