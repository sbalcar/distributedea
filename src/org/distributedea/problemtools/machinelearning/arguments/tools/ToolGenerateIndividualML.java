package org.distributedea.problemtools.machinelearning.arguments.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.ProblemMachineLearning;

public class ToolGenerateIndividualML {

	public static IndividualArguments generateIndividual(ProblemMachineLearning problemML,
			DatasetML datasetML, IAgentLogger logger) {
		
		ArgumentsDef arguments = problemML.getArgumentsDef();
		
		return new IndividualArguments(
				arguments.exportRandomGeneratedArguments());
	}
}
