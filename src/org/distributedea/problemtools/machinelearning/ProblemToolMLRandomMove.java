package org.distributedea.problemtools.machinelearning;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problemtools.machinelearning.arguments.operators.OperatorDifferential;
import org.distributedea.problemtools.machinelearning.arguments.operators.OperatorMoveABit;
import org.distributedea.problemtools.machinelearning.arguments.operators.OperatorMoveABitToTheRight;
import org.distributedea.problemtools.machinelearning.arguments.operators.OperatorSinglePointCrossover;
import org.distributedea.problemtools.machinelearning.arguments.tools.ToolGenerateFirstIndividualML;

public class ProblemToolMLRandomMove extends AProblemToolML {

	@Override
	public Arguments exportArguments() {
		return new Arguments();
	}

	@Override
	public void importArguments(Arguments arguments) {
		if (arguments == null || ! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
	}
	
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
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {
		
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
