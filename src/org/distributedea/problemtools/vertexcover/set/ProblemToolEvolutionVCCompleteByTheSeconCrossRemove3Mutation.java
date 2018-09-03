package org.distributedea.problemtools.vertexcover.set;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.problemtools.matrixfactorization.AProblemToolEvolutionMF;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorCompleteByTheSecondCrossover;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorRemoveRandom3;

public class ProblemToolEvolutionVCCompleteByTheSeconCrossRemove3Mutation extends AProblemToolEvolutionMF {

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
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem, Dataset dataset,
			IAgentLogger logger) throws Exception {
		
		IndividualSet individualSet1 = (IndividualSet) individual1;
		IndividualSet individualSet2 = (IndividualSet) individual2;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		return OperatorCompleteByTheSecondCrossover.crossover(individualSet1,
				individualSet2, problemVC, datasetVC);
	}

	@Override
	public Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualSet individualSet = (IndividualSet) individual;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		return OperatorRemoveRandom3.create(individualSet, problemVC, datasetVC,
				logger);
	}
	
}
