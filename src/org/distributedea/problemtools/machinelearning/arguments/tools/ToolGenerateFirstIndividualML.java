package org.distributedea.problemtools.machinelearning.arguments.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;

public class ToolGenerateFirstIndividualML {

	public static IndividualArguments generateIndividual(ProblemMachineLearning problemML,
			DatasetML datasetBP, IAgentLogger logger) {
		
		ArgumentsDef arguments = problemML.getArgumentsDef();
		
		return new IndividualArguments(
				arguments.exportMinArumentValues());
	}
}
