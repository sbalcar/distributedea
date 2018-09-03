package org.distributedea.problemtools.continuousoptimization.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.continuousoptimization.AProblemToolEvolutionCO;
import org.distributedea.problemtools.continuousoptimization.point.operators.OperatorMoveToSomewhereInTheMiddle;
import org.distributedea.problemtools.continuousoptimization.point.operators.OperatorRandomMove;

public class ProblemToolEvolutionCO extends AProblemToolEvolutionCO {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_Evolution.class);
		return agents;
	}

	@Override
	public Arguments exportArguments() {
		return new Arguments();
	}

	@Override
	public void importArguments(Arguments arguments) {
	}

	
	@Override
	public Individual improveIndividual(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
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
}
