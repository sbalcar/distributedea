package org.distributedea.problemtools.continuousoptimization.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problemtools.continuousoptimization.AProblemToolSimulatedAnnealingCO;
import org.distributedea.problemtools.continuousoptimization.point.operators.OperatorRandomMove;

public class ProblemToolSimulatedAnnealingCORandomMove extends AProblemToolSimulatedAnnealingCO {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_SimulatedAnnealing.class);
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
	public Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {

		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		return OperatorRandomMove.create(individualPoint, logger);
	}
}
