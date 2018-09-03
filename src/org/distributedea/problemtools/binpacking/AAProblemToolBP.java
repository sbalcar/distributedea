package org.distributedea.problemtools.binpacking;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problemtools.binpacking.permutation.tools.ToolBPNextFitFitness;
import org.distributedea.problemtools.binpacking.permutation.tools.ToolGenerateIndividualBinPacking;
import org.distributedea.problemtools.binpacking.permutation.tools.ToolReadProblemBinPacking;
import org.distributedea.problemtools.binpacking.permutation.tools.ToolReadSolutionBinPacking;
import org.distributedea.problemtools.tsp.point.AAProblemToolTSPPoint;

public abstract class AAProblemToolBP extends AAProblemToolTSPPoint {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetBinPacking.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPGPS.class;		
	}
	
	@Override
	public Class<?> reprezentationWhichUses() {
		
		return IndividualPermutation.class;
	}

	@Override
	public Dataset readDataset(IDatasetDescription datasetDescription, IProblem problem, IAgentLogger logger) {

		DatasetDescription datasetDescr = (DatasetDescription) datasetDescription;
		return ToolReadProblemBinPacking.readProblem(datasetDescr, problem, logger);
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
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		DatasetBinPacking problemBinPacking = (DatasetBinPacking) dataset;
		
		return ToolGenerateIndividualBinPacking.generateIndividual(problemBinPacking, logger);
	}
}
