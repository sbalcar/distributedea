package org.distributedea.problems.machinelearning;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.machinelearning.arguments.tools.ToolGenerateFirstIndividualML;
import org.distributedea.problems.machinelearning.arguments.operators.OperatorMoveABit;
import org.distributedea.problems.machinelearning.arguments.operators.OperatorMoveABitToTheRight;
import org.distributedea.problems.machinelearning.arguments.operators.OperatorSinglePointCrossover;
import org.distributedea.problems.machinelearning.arguments.operators.OperatorDifferential;

public class ProblemToolMLRandomMove extends AProblemToolML {

	@Override
	protected Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;
		DatasetML datasetML = (DatasetML) dataset; 
		
		return ToolGenerateFirstIndividualML.generateIndividual(problemML,
				datasetML, logger);
	}

	@Override
	protected Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, IAgentLogger logger) {
		
		IndividualArguments individualArgs = (IndividualArguments) individual;
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;
		
		return OperatorMoveABitToTheRight.create(individualArgs, problemML, logger);
	}

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualArguments individualArgs = (IndividualArguments) individual;
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;

		return OperatorMoveABit.create(individualArgs, problemML, logger);
	}

	@Override
	protected Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {

		IndividualArguments individualArgs = (IndividualArguments) individual;
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;

		return OperatorMoveABit.create(individualArgs, problemML, logger);
	}

	@Override
	protected Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualArguments individualArgs1 = (IndividualArguments) individual1;
		IndividualArguments individualArgs2 = (IndividualArguments) individual2;
		
		return OperatorSinglePointCrossover.create(individualArgs1, individualArgs2);
	}

	@Override
	protected Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualArguments individualArgs1 = (IndividualArguments) individual1;
		IndividualArguments individualArgs2 = (IndividualArguments) individual2;
		IndividualArguments individualArgs3 = (IndividualArguments) individual3;

		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;
		DatasetML datasetML = (DatasetML) dataset;
		
		return OperatorDifferential.create(individualArgs1, individualArgs2, individualArgs3,
				1, problemML, datasetML, logger);
	}

}
