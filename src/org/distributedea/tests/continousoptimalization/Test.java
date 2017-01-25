package org.distributedea.tests.continousoptimalization;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.co.f2.BatchSingleMethodsCOf2;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemdefinition.ProblemContinuousOptDef;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.continuousoptimization.ProblemToolCORandomMove;

public class Test {

	public static void main(String [] args) throws Exception {

		IInputBatch iBatchCOf2 = new BatchSingleMethodsCOf2();
		Batch batchCOf2 = iBatchCOf2.batch();
		
		ProblemTool tool = new ProblemToolCORandomMove();
		
		File file = batchCOf2.getJobs().get(0).exportDatasetFile();
		Dataset dataset = tool.readDataset(file, new TrashLogger());
		
		IProblemDefinition def = new ProblemContinuousOptDef(false);
		
		String fileOfSolution = FileNames.getDirectoryOfInputs() + File.separator +
				"solutions" + File.separator + file.getName();
		Individual bestIndividual = tool.readSolution(new File(fileOfSolution), dataset, new TrashLogger());
		double bestFitness = tool.fitness(bestIndividual, def, dataset, new TrashLogger());
		
		IndividualEvaluated individualEval =
				tool.generateIndividualEval(def, dataset, null, new TrashLogger());
		
		String fitness = "" + individualEval.getFitness();
		
		System.out.println("Best:   " + bestFitness);
		System.out.println("Random: " + fitness);
	}
}
