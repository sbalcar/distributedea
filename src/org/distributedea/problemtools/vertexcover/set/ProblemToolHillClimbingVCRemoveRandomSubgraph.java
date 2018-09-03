package org.distributedea.problemtools.vertexcover.set;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.problemtools.vertexcover.AProblemToolHillClimbingVC;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorRemoveRandomSubgraph;

public class ProblemToolHillClimbingVCRemoveRandomSubgraph extends AProblemToolHillClimbingVC {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_HillClimbing.class);
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
		
		IndividualSet individualSet = (IndividualSet) individual;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		return OperatorRemoveRandomSubgraph.create(individualSet, problemVC,
				datasetVC, logger);
	}
}
