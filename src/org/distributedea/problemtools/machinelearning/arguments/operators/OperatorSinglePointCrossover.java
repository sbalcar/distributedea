package org.distributedea.problemtools.machinelearning.arguments.operators;

import java.util.Random;

import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.individuals.IndividualArguments;

public class OperatorSinglePointCrossover {

	public static IndividualArguments[] create(IndividualArguments individualArguments1,
			IndividualArguments individualArguments2) {
		
		Arguments arguments1 = individualArguments1.getArguments();
		Arguments arguments2 = individualArguments2.getArguments();
		
		Random rnd = new Random();
		int bound = rnd.nextInt(arguments1.size());
			
		Arguments argumentsNew1 = new Arguments();
		Arguments argumentsNew2 = new Arguments();
		for (int i = 0; i < arguments1.size(); i++) {
			
			Argument argumentFor1 = null;
			Argument argumentFor2 = null;
			
			if (i < bound) {
				argumentFor1 = arguments1.getArguments().get(i);
				argumentFor2 = arguments2.getArguments().get(i);
			} else {
				argumentFor1 = arguments2.getArguments().get(i);
				argumentFor2 = arguments1.getArguments().get(i);
			}
			
			argumentsNew1.addArgument(argumentFor1);
			argumentsNew2.addArgument(argumentFor2);
		}
		
		IndividualArguments[] result = new IndividualArguments[2];
		result[0] = new IndividualArguments(argumentsNew1);
		result[1] = new IndividualArguments(argumentsNew2);
		
		return result;
	}
}
