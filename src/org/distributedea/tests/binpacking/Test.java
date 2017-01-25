package org.distributedea.tests.binpacking;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.binpacking.objects1000.BatchSingleMethodsBPP1000;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemdefinition.ProblemBinPackingDef;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.binpacking.permutation.ProblemToolBinPackingSimpleShift;

public class Test {

	public static void main(String [ ] args) throws Exception {
	
		IInputBatch iInputBatch = new BatchSingleMethodsBPP1000();
		Batch batch = iInputBatch.batch();
		
		ProblemTool tool = new ProblemToolBinPackingSimpleShift();
		
		File file = batch.getJobs().get(0).exportDatasetFile();
		Dataset problem = tool.readDataset(file, new TrashLogger());
		
		IProblemDefinition def = new ProblemBinPackingDef(1);
		
		IndividualEvaluated individual =
				tool.generateIndividualEval(def, problem, null, new TrashLogger());

		String fileOfSolution = FileNames.getDirectoryOfInputs() + File.separator +
				"solutions" + File.separator + file.getName();
		Individual bestIndividual = tool.readSolution(new File(fileOfSolution), problem, new TrashLogger());
		double fitness = tool.fitness(bestIndividual, def, problem, new TrashLogger());
		
		System.out.println(individual.getFitness());
		System.out.println(fitness);
	}
}
