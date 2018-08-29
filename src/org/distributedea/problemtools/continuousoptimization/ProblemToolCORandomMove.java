package org.distributedea.problemtools.continuousoptimization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problemtools.ProblemTool;
import org.distributedea.problemtools.continuousoptimization.point.operators.OperatorDifferential;
import org.distributedea.problemtools.continuousoptimization.point.operators.OperatorMoveToSomewhereInTheMiddle;
import org.distributedea.problemtools.continuousoptimization.point.operators.OperatorRandomMove;
import org.distributedea.problemtools.continuousoptimization.point.tools.ToolGenerateNextIndividualCO;

/**
 * Random move {@link ProblemTool}
 * @author stepan
 *
 */
public class ProblemToolCORandomMove extends AProblemToolCO {

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
	public Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		return generateIndividual(problem, dataset, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {

		ProblemContinuousOpt problemCO = (ProblemContinuousOpt)problem;
		DatasetContinuousOpt datasetCO = (DatasetContinuousOpt) dataset;
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return ToolGenerateNextIndividualCO.create(individualPoint,
				problemCO, datasetCO, logger);
	}

	@Override
	public Individual improveIndividual(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, logger);
	}

	@Override
	public Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, logger);
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;

		return OperatorMoveToSomewhereInTheMiddle.create(individualP1, individualP2,
				logger);
	}

	@Override
	public Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;
		IndividualPoint individualP3 = (IndividualPoint) individual3;
		
		return OperatorDifferential.create(individualP1, individualP2,
				individualP3, 1, dataset, logger);
	}

}
