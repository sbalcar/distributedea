package org.distributedea.problems.binpacking.permutation;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.binpacking.ProblemBinPackingTool;
import org.distributedea.problems.binpacking.permutation.tools.ToolFitnessBinPacking;
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
	public Problem readProblem(File fileOfProblem, IAgentLogger logger) {

		return ToolReadProblemBinPacking.readProblem(fileOfProblem, logger);
	}

	@Override
	public Individual readSolution(File fileOfSolution, Problem problem,
			IAgentLogger logger) {

		return ToolReadSolutionBinPacking.readSolution(fileOfSolution, logger);
	}

	@Override
	public double fitness(Individual individual, IProblemDefinition problemDef, Problem problem,
			IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		ProblemBinPacking problemBinPacking = (ProblemBinPacking) problem;
		
		return ToolFitnessBinPacking.evaluate(individualPerm, problemBinPacking, logger);
	}

	@Override
	protected Individual generateIndividual(IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) {

		ProblemBinPacking problemBinPacking = (ProblemBinPacking) problem;
		
		return ToolGenerateIndividualBinPacking.generateIndividual(problemBinPacking, logger);
	}

	@Override
	protected Individual generateFirstIndividual(IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger) {

		ProblemBinPacking problemBinPacking = (ProblemBinPacking) problem;

		return ToolGenerateFirstIndividualBinPacking.generateFirstIndividual(problemBinPacking, logger);
	}

	@Override
	protected Individual generateNextIndividual(IProblemDefinition problemDef,
			Problem problem, Individual individual, IAgentLogger logger) {

		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		
		return ToolNextPermutationBinPacking.nextPermutation(individualPerm);
	}

}
