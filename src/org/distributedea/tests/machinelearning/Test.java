package org.distributedea.tests.machinelearning;

import java.util.ArrayList;
import java.util.Arrays;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.co.bbobf08.BatchSingleMethodsCOf08;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.continuousoptimization.ProblemToolCORandomMove;

public class Test {

	public static void main(String [] args) throws Exception {
		
		IInputBatch inputBatch = new BatchSingleMethodsCOf08();
		Batch batch = inputBatch.batch();
		
		Job job = batch.getJobs().get(0);
		
		IProblem problem = job.getProblem();
				
		ProblemTool tool = new ProblemToolCORandomMove();
		
		Dataset dataset = tool.readDataset(job.exportDatasetFile(), problem, 
				new TrashLogger());
		
		IndividualPoint individual = new IndividualPoint(
				new ArrayList<>(Arrays.asList(0.0, 0.0)) );
		
		tool.fitness(individual, problem, dataset, new TrashLogger());
		
		double fitness = tool.fitness(individual, problem, dataset, new TrashLogger());
		System.out.println("Fitness: " + fitness);
	}
}
