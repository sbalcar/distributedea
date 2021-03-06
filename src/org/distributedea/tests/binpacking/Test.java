package org.distributedea.tests.binpacking;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.binpacking.objects1000.BatchSingleMethodsBPP1000;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemBinPacking;
import org.distributedea.problems.AProblemTool;
import org.distributedea.problems.binpacking.permutation.ProblemToolBruteForceBP;

public class Test {

	public static void main(String [ ] args) throws Exception {
	
		IInputBatch iInputBatch = new BatchSingleMethodsBPP1000();
		Batch batch = iInputBatch.batch();
		
		AProblemTool tool = new ProblemToolBruteForceBP();
		
		IDatasetDescription datasetDescr = batch.getJobs().get(0).getDatasetDescription();
		Dataset dataset = tool.readDataset(datasetDescr, null, new TrashLogger());
		
		IProblem problem = new ProblemBinPacking(1);
		
		IndividualEvaluated individual =
				tool.generateIndividualEval(problem, dataset, null, new TrashLogger());

		String fileOfSolution = FileNames.getDirectoryOfInputs() + File.separator +
				"solutions" + File.separator + datasetDescr.toLogString();
		Individual bestIndividual = tool.readSolution(new File(fileOfSolution), dataset, new TrashLogger());
		double fitness = tool.fitness(bestIndividual, problem, dataset, new TrashLogger());
		
		System.out.println(individual.getFitness());
		System.out.println(fitness);
	}
}
