package org.distributedea.problems.machinelearning.arguments;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.machinelearning.AProblemToolEvolutionML;
import org.distributedea.problems.machinelearning.arguments.operators.OperatorMoveABit;
import org.distributedea.problems.machinelearning.arguments.operators.OperatorSinglePointCrossover;

public class ProblemToolEvolutionML extends AProblemToolEvolutionML {

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
	public Individual mutationOfIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualArguments individualArgs = (IndividualArguments) individual;
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;

		return OperatorMoveABit.create(individualArgs, problemML, logger);
	}

	@Override
	public Individual[] crossIndividual(Individual individual1,
			Individual individual2, IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualArguments individualArgs1 = (IndividualArguments) individual1;
		IndividualArguments individualArgs2 = (IndividualArguments) individual2;
		
		return OperatorSinglePointCrossover.create(individualArgs1, individualArgs2);
	}
	
}
