package org.distributedea.problems.binpacking.permutation;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.binpacking.ProblemBinPackingTool;
import org.distributedea.problems.binpacking.permutation.tools.ToolBPNextFitFitness;
import org.distributedea.problems.binpacking.permutation.tools.ToolGenerateFirstIndividualBinPacking;
import org.distributedea.problems.binpacking.permutation.tools.ToolGenerateIndividualBinPacking;
import org.distributedea.problems.binpacking.permutation.tools.ToolNextPermutationBinPacking;
import org.distributedea.problems.binpacking.permutation.tools.ToolReadProblemBinPacking;
import org.distributedea.problems.binpacking.permutation.tools.ToolReadSolutionBinPacking;

public abstract class AProblemToolBinPackingPermutation extends ProblemBinPackingTool {

	@Override
	public Class<?> reprezentationWhichUses() {
		
		return IndividualPermutation.class;
	}

	@Override
	public Dataset readDataset(File fileOfProblem, IAgentLogger logger) {

		return ToolReadProblemBinPacking.readProblem(fileOfProblem, logger);
	}

	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {

		return ToolReadSolutionBinPacking.readSolution(fileOfSolution, logger);
	}

	@Override
	public double fitness(Individual individual, IProblem problem, Dataset dataset,
			IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		DatasetBinPacking problemBinPacking = (DatasetBinPacking) dataset;
		
		return ToolBPNextFitFitness.evaluate(individualPerm, problemBinPacking, logger);
	}

	@Override
	protected Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		DatasetBinPacking problemBinPacking = (DatasetBinPacking) dataset;
		
		return ToolGenerateIndividualBinPacking.generateIndividual(problemBinPacking, logger);
	}

	@Override
	protected Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		DatasetBinPacking problemBinPacking = (DatasetBinPacking) dataset;

		return ToolGenerateFirstIndividualBinPacking.generateFirstIndividual(problemBinPacking, logger);
	}

	@Override
	protected Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, IAgentLogger logger) {

		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return ToolNextPermutationBinPacking.nextPermutation(individualPerm);
	}

}
