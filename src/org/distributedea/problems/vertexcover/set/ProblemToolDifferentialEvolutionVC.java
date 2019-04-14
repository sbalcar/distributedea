package org.distributedea.problems.vertexcover.set;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.problems.matrixfactorization.AProblemToolDifferentialEvolutionMF;
import org.distributedea.problems.vertexcover.set.operators.OperatorCompleteByTheSecondCrossover;
import org.distributedea.problems.vertexcover.set.operators.OperatorDifferential;

public class ProblemToolDifferentialEvolutionVC extends AProblemToolDifferentialEvolutionMF {

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
	public Individual differentialOfIndividuals(Individual individual1,
			Individual individual2, Individual individual3, IProblem problem,
			Dataset dataset, IAgentLogger logger) throws Exception {
		
		IndividualSet individualSet1 = (IndividualSet) individual1;
		IndividualSet individualSet2 = (IndividualSet) individual2;
		IndividualSet individualSet3 = (IndividualSet) individual3;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;
		
		return OperatorDifferential.create(individualSet1, individualSet2,
				individualSet3, 1, problemVC, datasetVC, logger);
	}

	@Override
	public Individual cross(Individual individualOld, Individual individualNew,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualSet individualSetOld = (IndividualSet) individualOld;
		IndividualSet individualSetNew = (IndividualSet) individualNew;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		Individual[] individualsCreated = OperatorCompleteByTheSecondCrossover.crossover(
				individualSetOld, individualSetNew, problemVC, datasetVC);
		
		return individualsCreated[0];
	}	
	
}
