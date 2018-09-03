package org.distributedea.problemtools.tsp.gps;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolFitnessTSPGPS;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolGenerateIndividualTSPGPS;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolReadProblemTSPGPS;
import org.distributedea.problemtools.tsp.gps.permutation.tools.ToolReadSolutionTSPGPS;
import org.distributedea.problemtools.tsp.point.AAProblemToolTSPPoint;

public abstract class AAProblemToolTSPGPS extends AAProblemToolTSPPoint {

	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPGPS.class;
	}

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetTSPGPS.class;
	}
		
	@Override
	public Class<?> reprezentationWhichUses() {
	
		return IndividualPermutation.class;
	}
	
	@Override
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		DatasetTSPGPS problemTSP = (DatasetTSPGPS) dataset;
		
		return ToolGenerateIndividualTSPGPS.generateIndividual(problemTSP, logger);
	}
	
	@Override
	public Dataset readDataset(IDatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {

		DatasetDescription datasetDescr = (DatasetDescription) datasetDescription;
		return ToolReadProblemTSPGPS.readDataset(datasetDescr, problem, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {

		return ToolReadSolutionTSPGPS.readSolution(fileOfSolution, logger);
	}
	
	@Override
	public double fitness(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		DatasetTSPGPS problemTSP = (DatasetTSPGPS) dataset;

	    return ToolFitnessTSPGPS.evaluate(individualPerm, problemTSP, logger);
	}
}
