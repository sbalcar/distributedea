package org.distributedea.problemtools.machinelearning.arguments;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problemtools.machinelearning.AProblemToolDifferentialEvolutionML;
import org.distributedea.problemtools.machinelearning.arguments.operators.OperatorDifferential;

public class ProblemToolDifferentialEvolutionML extends AProblemToolDifferentialEvolutionML {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_DifferentialEvolution.class);
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
	public Individual createNewIndividual(Individual individual1,
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
