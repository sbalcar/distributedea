package org.distributedea.problems.vertexcover;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.vertexcover.set.tools.ToolFitnessVC;
import org.distributedea.problems.vertexcover.set.tools.ToolGenerateIndividualVC;
import org.distributedea.problems.vertexcover.set.tools.ToolReadProblemVC;
import org.distributedea.problems.vertexcover.set.tools.ToolReadSolutionVC;

public abstract class AProblemToolVC extends ProblemTool {

	@Override
	public Class<?> datasetReprezentation() {
		return DatasetVertexCover.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemVertexCover.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualSet.class;
	}
	
	@Override
	public void initialization(IProblem problem, Dataset dataset,
			AgentConfiguration agentConf, IAgentLogger logger) throws Exception {
	}
	
	@Override
	public void exit() throws Exception {
	}
	
	@Override
	public double fitness(Individual individual, IProblem problem, Dataset dataset,
			IAgentLogger logger) {
		
		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		IndividualSet individualSet = (IndividualSet) individual;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;
		
		return ToolFitnessVC.evaluate(individualSet, problemVC, datasetVC, logger);
	}
	
	@Override
	public Dataset readDataset(File fileOfProblem, IAgentLogger logger) {
		
		return ToolReadProblemVC.readProblem(fileOfProblem, logger);
	}
	
	@Override
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		ProblemVertexCover problemVC = (ProblemVertexCover) problem;
		DatasetVertexCover datasetVC = (DatasetVertexCover) dataset;
		
		return ToolGenerateIndividualVC.generateIndividual(problemVC, datasetVC, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionVC.readSolution(fileOfSolution, logger);
	}

	public AProblemToolVC deepClone() {
		return this;
	}
}
