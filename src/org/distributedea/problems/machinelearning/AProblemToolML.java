package org.distributedea.problems.machinelearning;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetML;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.machinelearning.arguments.tools.ToolFitnessML;
import org.distributedea.problems.machinelearning.arguments.tools.ToolGenerateIndividualML;
import org.distributedea.problems.machinelearning.arguments.tools.ToolReadProblemML;
import org.distributedea.problems.machinelearning.arguments.tools.ToolReadSolutionML;

public abstract class AProblemToolML extends ProblemTool {
	
	@Override
	public Class<?> datasetReprezentation() {
		return DatasetML.class;
	}
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemMachineLearning.class;
	}

	@Override
	public Class<?> reprezentationWhichUses() {
		return IndividualArguments.class;
	}

	@Override
	public void initialization(Dataset dataset, AgentConfiguration agentConf,
			IAgentLogger logger) throws Exception {
	}

	@Override
	public void exit() throws Exception {
	}
	
	@Override
	public double fitness(Individual individual, IProblem problem, Dataset dataset,
			IAgentLogger logger) {
		
		IndividualArguments individualArg = (IndividualArguments) individual;
		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;
		DatasetML datasetML = (DatasetML) dataset;
		
		try {
			return ToolFitnessML.evaluate(individualArg, problemML, datasetML, logger);
		} catch (Exception e) {
			return Double.NaN;
		}
	}
	
	@Override
	public Dataset readDataset(File fileOfProblem, IAgentLogger logger) {
		
		return ToolReadProblemML.readProblem(fileOfProblem, logger);
	}
	
	@Override
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		ProblemMachineLearning problemML = (ProblemMachineLearning) problem;
		DatasetML datasetML = (DatasetML) dataset;
		
		return ToolGenerateIndividualML.generateIndividual(problemML, datasetML, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionML.readSolution(fileOfSolution, logger);
	}

}
