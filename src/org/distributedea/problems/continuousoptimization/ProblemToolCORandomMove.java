package org.distributedea.problems.continuousoptimization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorDifferential;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorRandomMove;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorMoveToSomewhereInTheMiddle;
import org.distributedea.problems.continuousoptimization.point.tools.ToolGenerateNextIndividualCO;

/**
 * Random move {@link ProblemTool}
 * @author stepan
 *
 */
public class ProblemToolCORandomMove extends AProblemToolCO {

	@Override
	public Individual generateFirstIndividual(IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) {
		
		return generateIndividual(problemDef, dataset, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblemDefinition problemDef,
			Dataset dataset, Individual individual, IAgentLogger logger) {

		DatasetContinuousOpt probelmCO = (DatasetContinuousOpt) dataset;
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return ToolGenerateNextIndividualCO.create(probelmCO, individualPoint, logger);
	}

	@Override
	public Individual improveIndividual(Individual individual, IProblemDefinition problemDef,
			IAgentLogger logger) throws Exception {
		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, logger);
	}

	@Override
	public Individual getNeighbor(Individual individual,
			IProblemDefinition problemDef, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, logger);
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblemDefinition problemDef,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;

		return OperatorMoveToSomewhereInTheMiddle.create(individualP1, individualP2,
				logger);
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			IProblemDefinition problemDef, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;
		IndividualPoint individualP3 = (IndividualPoint) individual3;
		
		Individual[] individual = new Individual[1];
		individual[0] = OperatorDifferential.create(individualP1, individualP2,
				individualP3, 1, dataset, logger);
		
		return individual;
	}

}
