package org.distributedea.problemtools.vertexcover.set;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.problemtools.vertexcover.AProblemToolBruteForceVC;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorSequentiallyGeneratingSubsets;

public class ProblemToolBruteForceVC extends AProblemToolBruteForceVC {

	OperatorSequentiallyGeneratingSubsets opr = new OperatorSequentiallyGeneratingSubsets();

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
		
		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;
		
		return opr.generateNextIndividual(null, problemVC, datasetVC, logger);
	}

	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {

		IndividualSet individualSet = (IndividualSet) individual;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		return opr.generateNextIndividual(individualSet, problemVC,
				datasetVC, logger);
	}

}
