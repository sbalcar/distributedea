package org.distributedea.problems.machinelearning.arguments;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.machinelearning.AProblemToolBruteForceML;
import org.distributedea.problems.machinelearning.arguments.operators.OperatorMoveABitToTheRight;
import org.distributedea.problems.machinelearning.arguments.tools.ToolGenerateFirstIndividualML;


public class ProblemToolBruteForceML extends AProblemToolBruteForceML {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_BruteForce.class);
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
	public Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;
		DatasetML datasetML = (DatasetML) dataset; 
		
		return ToolGenerateFirstIndividualML.generateIndividual(problemML,
				datasetML, logger);
		
	}

	@Override
	public Individual generateNextIndividual(IProblem problem, Dataset dataset,
			Individual individual, long neighborIndex, IAgentLogger logger) {

		IndividualArguments individualArgs = (IndividualArguments) individual;
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;
		
		return OperatorMoveABitToTheRight.create(individualArgs, problemML, logger);
	}
	
}
