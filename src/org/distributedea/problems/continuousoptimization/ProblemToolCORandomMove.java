package org.distributedea.problems.continuousoptimization;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorMoveInTheMiddle;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorRandomMove;
import org.distributedea.problems.continuousoptimization.point.operators.OperatorMoveToSomewhereInTheMiddle;
import org.distributedea.problems.continuousoptimization.point.tools.ToolGenerateNextIndividualCO;

public class ProblemToolCORandomMove extends AProblemToolCO {

	@Override
	public Individual generateFirstIndividual(IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) {
		
		return generateIndividual(problemDef, problem, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblemDefinition problemDef,
			Problem problem, Individual individual, IAgentLogger logger) {

		ProblemContinuousOpt probelmCO = (ProblemContinuousOpt) problem;
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
			IProblemDefinition problemDef, Problem problem,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, logger);
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;

		return OperatorMoveToSomewhereInTheMiddle.create(individualP1, individualP2,
				logger);
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			IProblemDefinition problemDef, Problem problem,
			IAgentLogger logger) throws Exception {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;
		IndividualPoint individualP3 = (IndividualPoint) individual3;
		
		return OperatorMoveInTheMiddle.create(individualP1, individualP2,
				individualP3, problem, logger);
	}

}
