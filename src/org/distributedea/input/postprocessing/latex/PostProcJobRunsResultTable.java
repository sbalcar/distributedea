package org.distributedea.input.postprocessing.latex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.batches.tsp.cities1083.BatchSingleMethodsTSP1083;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.IProblem;

public class PostProcJobRunsResultTable extends PostProcessing {

	private int maxLengthOfResult;
	
	public PostProcJobRunsResultTable(int maxLengthOfResult) {
		if (maxLengthOfResult <= 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is nto valid");
		}
		this.maxLengthOfResult = maxLengthOfResult;
	}
	
	@Override
	public void run(Batch batch) throws Exception {
		// sorting jobs
		batch.sortJobsByID();

		String BATCH_ID = batch.getBatchID();
		String BATCH_DESCRIPTION = batch.getDescription();
		int NUMBER_OF_RUNS = batch.getJobs().get(0).getNumberOfRuns();
		
		String NL = "\n";
		List<Job> jobs = batch.getJobs();
		
		String latexCode =
		"\\begin{table} [ht] " + NL +
		"\\centering" + NL;

		
		String table = "|l|";
		for (int i = 1; i <= jobs.size(); i++) {
			table += "c|";
		}
		
		latexCode +=
		"\\begin{tabular}{ " + table + " }" + NL + 
		"\\hline" + NL;
		
		latexCode +=
		"\\textbf{Identifikace job struktur} & \\multicolumn{" + NUMBER_OF_RUNS + "}"
				+ "{|c|}{\\textbf{Výsledky běhů 1-" + NUMBER_OF_RUNS + "}} \\\\" + NL +
		"\\hline\\hline" + NL;
		
		for (Job jobI : jobs) {
			latexCode += processJob(jobI, BATCH_ID) + NL;
		}
		
		latexCode +=
		"\\hline" + NL +
		"\\end{tabular}" + NL +
		"\\caption{" + BATCH_DESCRIPTION + "}" + NL +
		"\\label{tab:" + BATCH_ID + "Results}" + NL +
		"\\end{table}";

		
		System.out.println(latexCode);
		
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(BATCH_ID);
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "");
		
		try(  PrintWriter out = new PrintWriter(OUTPUT_PATH + File.separator + OUTPUT_FILE + ".lat")  ){
		    out.println(latexCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}


	private String processJob(Job job, String batchID) throws IOException {
		
		String jobID = job.getJobID();	
		int numberOfRuns = job.getNumberOfRuns();
		IProblem problem = job.getProblem();
		
		Map<JobID, Double> resultsOfJobsMap = FilesystemTool
				.getTheBestPartResultOfJobForAllRuns(batchID, jobID, numberOfRuns, problem);
		
		List<Double> resultsOfJobs = new ArrayList<>(resultsOfJobsMap.values());

		String jobLine = job.getDescription().replaceAll("\\&", "\\\\&");
		for (double resultI : resultsOfJobs) {
			
			String resultStrI = "";
			
			// convert double to format without exponent
			resultStrI = String.format("%." + maxLengthOfResult + "f", resultI);
			
			// cut to given size
			resultStrI = resultStrI.substring(0, maxLengthOfResult);
			
			// remove zeroes at the end
			if (resultStrI.contains(".") || resultStrI.contains(",")) {
				resultStrI = resultStrI.replaceAll("[0]*$", "");
				resultStrI = resultStrI.replaceAll("\\.$", "");
				resultStrI = resultStrI.replaceAll("\\,$", "");
			}
			
			jobLine += " & " + resultStrI;
		}

		return jobLine + " \\\\";
	}
	
	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchSingleMethodsTSP1083();
//		IInputBatch batchCmp = new BatchHomoMethodsTSP1083();
		
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcJobRunsResultTable(10);
		p.run(batch);
		
	}
}
