package org.distributedea.input.postprocessing.matrixfactorization;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJobRunResults;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problems.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;


public class PostProcMFTestSetTableOfJobRunResults extends PostProcTableOfJobRunResults {

	public PostProcMFTestSetTableOfJobRunResults(int maxLengthOfResult) {
		super(maxLengthOfResult);
	}

	protected Map<JobID, Double> readResultsOfJob(String batchID,
			int numberOfRuns, Job job) throws Exception {
		
		String jobID = job.getJobID();
		IProblem problem = job.getProblem();
		
		ProblemMatrixFactorization problemMF =
				(ProblemMatrixFactorization) problem;
		
		DatasetMF datasetTestMF = ToolReadDatasetMF.readTestingPartOfDataset(
				job.exportDatasetFile(), problemMF, new TrashLogger());
		
		Map<JobID, Double> results = new HashMap<>();
		
		for (int runI = 0; runI < numberOfRuns; runI++) {
			
			JobID jobIdI = new JobID(batchID, jobID, runI);
			String fileName = FileNames.getResultSolutionFile(jobIdI);
			
			IndividualEvaluated indivEvalI =
					IndividualEvaluated.importXML(new File(fileName));

			IndividualLatentFactors individualLFI =
					(IndividualLatentFactors) indivEvalI.getIndividual();
			
			double fitnessResult = ToolFitnessRMSEMF.evaluate(individualLFI,
					problemMF, datasetTestMF, new TrashLogger());

			results.put(jobIdI, fitnessResult);
		}
		
		return results;
	}
}
