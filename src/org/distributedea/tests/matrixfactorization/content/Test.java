package org.distributedea.tests.matrixfactorization.content;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.jobs.InputMatrixFactorization;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemToolEvolution;
import org.distributedea.problems.matrixfactorization.latentfactor.ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation;

public class Test {

	public static void main(String [ ] args) throws Exception {
				
		System.out.println("Test content");
		
//		Job job = InputMatrixFactorization.test01(); // 100k
//		Job job = InputMatrixFactorization.test02(); // 1M
		Job job = InputMatrixFactorization.test03(); // 10M
		
		IProblem problem = job.getProblem();
		IDatasetDescription datasetDescription = job.getDatasetDescription();
		
		IProblemToolEvolution problemTool = (IProblemToolEvolution) new ProblemToolEvolutionMFUniformCrossSGDist1RandomMutation();
		DatasetMF datasetMF = (DatasetMF) problemTool.readDataset(datasetDescription,
				problem, new TrashLogger());
		
		IndividualEvaluated indivEvalGenerated = problemTool.generateIndividualEval(
				problem, datasetMF, null, new TrashLogger());
		
		System.out.println("Generated individual:");
		System.out.println("Fitness: " + indivEvalGenerated.getFitness());
		
		IndividualEvaluated indivEvalImproved = problemTool.mutationOfIndividualEval(
				indivEvalGenerated, problem, datasetMF, null, new TrashLogger());
		
		
		System.out.println("");
		System.out.println("Improved individual:");
		System.out.println("Fitness: " + indivEvalImproved.getFitness());
		
		System.out.println("OK");
	}
}
