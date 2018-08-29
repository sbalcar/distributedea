package org.distributedea.problemtools.vertexcover.set;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.problemtools.vertexcover.AProblemToolVC;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorCompleteByTheSecondCrossover;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorDifferential;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorRemoveRandom3;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorRemoveRandomSubgraph;
import org.distributedea.problemtools.vertexcover.set.operators.OperatorSequentiallyGeneratingSubsets;

public class ProblemToolVC extends AProblemToolVC {

	OperatorSequentiallyGeneratingSubsets opr = new OperatorSequentiallyGeneratingSubsets();
	
	@Override
	public Arguments exportArguments() {
		return new Arguments();
	}

	@Override
	public void importArguments(Arguments arguments) {
		if (arguments == null || ! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
	}
	
	@Override
	protected Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;
		
		return opr.generateNextIndividual(null, problemVC, datasetVC, logger);
	}

	@Override
	protected Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {

		IndividualSet individualSet = (IndividualSet) individual;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		return opr.generateNextIndividual(individualSet, problemVC,
				datasetVC, logger);
	}

	@Override
	protected Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception {

		IndividualSet individualSet = (IndividualSet) individual;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		return OperatorRemoveRandom3.create(individualSet, problemVC, datasetVC,
				logger);
	}

	@Override
	protected Individual getNeighbor(Individual individual, IProblem problem,
			Dataset dataset, long neighborIndex, IAgentLogger logger)
			throws Exception {
		
		IndividualSet individualSet = (IndividualSet) individual;

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;

		return OperatorRemoveRandomSubgraph.create(individualSet, problemVC,
				datasetVC, logger);
	}

	@Override
	protected Individual[] createNewIndividual(Individual individual1,
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
	protected Individual createNewIndividual(Individual individual1,
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

}
