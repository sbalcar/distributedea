package org.distributedea.input.postprocessing.matrixfactorization;

import java.io.IOException;
import java.util.Map;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJobRunResults;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.IProblem;


public class PostProcMFTestSetTableOfJobRunResults extends PostProcTableOfJobRunResults {

	public PostProcMFTestSetTableOfJobRunResults(int maxLengthOfResult) {
		super(maxLengthOfResult);
	}

	protected Map<JobID, Double> readResultsOfJob(String batchID,
			int numberOfRuns, Job job) throws IOException {
		
		String jobId = job.getJobID();
		IProblem problem = job.getProblem();
		
		return FilesystemTool.getTheBestPartResultOfJobForAllRuns(
				batchID, jobId, numberOfRuns, problem);
	}
}
