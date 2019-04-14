package org.distributedea.problems.machinelearning.arguments.operators;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.argumentsdefinition.ArgumentDef;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;

public class OperatorMoveABit {
	
	private static double maxStep = 0.005;
	
	public static Individual create(IndividualArguments individualArgs,
			ProblemMachineLearning problemML, IAgentLogger logger) {

		// definition of arguments and selection of the one argument definition
		ArgumentsDef argumentsDef = problemML.getArgumentsDef().
				exportsArgumentsWithoutRestrictionForFixedValue();
		ArgumentDef argumentDef = argumentsDef.exportRandomArgumentDef();
		
		// arguments and exports argument value for the selected argument definition
		Arguments arguments = individualArgs.getArguments();
		Argument argument = arguments.exportArgument(argumentDef.getName());
		
		Argument argumentNew = argumentDef.exportNeighbourValue(argument, maxStep);
		Argument argumentNewCor = argumentDef.exportCorrectedValue(argumentNew);
		
		Arguments argumentsNew = arguments.deepClone();
		argumentsNew.replaceArgument(argumentNewCor);
		
		return new IndividualArguments(argumentsNew);
	}
}
