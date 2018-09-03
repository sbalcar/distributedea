package org.distributedea.problemtools.machinelearning.arguments;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problemtools.machinelearning.AProblemToolTabuSearchML;
import org.distributedea.problemtools.machinelearning.arguments.operators.OperatorMoveABit;

public class ProblemToolTabuSearchMLMoveABit extends AProblemToolTabuSearchML {
	
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
	public Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {

		IndividualArguments individualArgs = (IndividualArguments) individual;
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;

		return OperatorMoveABit.create(individualArgs, problemML, logger);
	}

}