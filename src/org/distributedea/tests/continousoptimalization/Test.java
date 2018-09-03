package org.distributedea.tests.continousoptimalization;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.co.f2.BatchSingleMethodsCOf2;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problemtools.AProblemTool;
import org.distributedea.problemtools.continuousoptimization.point.ProblemToolBruteForceCO;

public class Test {

	public static void main(String [] args) throws Exception {

		IInputBatch iBatchCOf2 = new BatchSingleMethodsCOf2();
		Batch batchCOf2 = iBatchCOf2.batch();
		
		AProblemTool tool = new ProblemToolBruteForceCO();
		
		IDatasetDescription datasetDescr = batchCOf2.getJobs().get(0).getDatasetDescription();
		Dataset dataset = tool.readDataset(datasetDescr, null, new TrashLogger());
		
		IProblem problem = new ProblemContinuousOpt("f2", 2, false);
		
		String fileOfSolution = FileNames.getDirectoryOfInputs() + File.separator +
				"solutions" + File.separator + datasetDescr.toLogString();
		Individual bestIndividual = tool.readSolution(new File(fileOfSolution), dataset, new TrashLogger());
		double bestFitness = tool.fitness(bestIndividual, problem, dataset, new TrashLogger());
		
		IndividualEvaluated individualEval =
				tool.generateIndividualEval(problem, dataset, null, new TrashLogger());
		
		String fitness = "" + individualEval.getFitness();
		
		System.out.println("Best:   " + bestFitness);
		System.out.println("Random: " + fitness);
	}
}
