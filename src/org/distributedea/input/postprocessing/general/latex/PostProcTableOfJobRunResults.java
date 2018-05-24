package org.distributedea.input.postprocessing.general.latex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
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

public class PostProcTableOfJobRunResults extends PostProcessing {

	private int maxLengthOfResult;
	
	public PostProcTableOfJobRunResults(int maxLengthOfResult) {
		if (maxLengthOfResult <= 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is nto valid");
		}
		this.maxLengthOfResult = maxLengthOfResult;
	}
	
	@Override
	public void run(Batch batch) throws Exception {
		
		String NL = "\n";
		
		// sorting jobs
		batch.sortJobsByID();

		String BATCH_ID = batch.getBatchID();
		
		
		String latexStaistic = createStatisticTableWithResults(batch);
		
		String latexAllValues = createTableWithResults(batch);
		
		String latexResult = latexStaistic + NL + NL + latexAllValues + NL;
		
		
		System.out.println(latexResult);
		
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(BATCH_ID);
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "");
		
		try(  PrintWriter out = new PrintWriter(OUTPUT_PATH + File.separator + OUTPUT_FILE + ".lat")  ){
		    out.println(latexResult);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private String createStatisticTableWithResults(Batch batch) throws Exception {
		
		String BATCH_ID = batch.getBatchID();
		String BATCH_DESCRIPTION = batch.getDescription();
		int NUMBER_OF_RUNS = batch.getJobs().get(0).getNumberOfRuns();
		
		String NL = "\n";
		List<Job> jobs = batch.getJobs();
		
		String latexCode =
		"\\begin{table} [ht]" + NL +
		"\\centering" + NL +
		"\\begin{tabular}{ |l|r|r|r|r| }" + NL +
		"\\hline" + NL +
		"\\textbf{Identifikace job struktur} & Min & Max & Medián & Průměr \\\\" + NL +
		"\\hline\\hline" + NL;

		for (Job jobI : jobs) {
			
			Map<JobID, Double> resultsOfJobsMap = readResultsOfJob(
					BATCH_ID, NUMBER_OF_RUNS, jobI);

			List<Double> resultsOfJobI = new ArrayList<Double>(
					resultsOfJobsMap.values());
			
			double min = Collections.min(resultsOfJobI);
			double max = Collections.max(resultsOfJobI);
			
			Collections.sort(resultsOfJobI);
			double median = resultsOfJobI.get((resultsOfJobI.size() -1) /2);
			
			double avrg = 0;
			for (double resultI : resultsOfJobI) {
				avrg += resultI;
			}
			avrg = avrg / resultsOfJobI.size();
			
			latexCode +=
					jobI.getDescription().replace("&", "\\&") + " & " +
					convertDoubleToString(min) + " & " +
					convertDoubleToString(max) + " & " +
					convertDoubleToString(median) + " & " +
					convertDoubleToString(avrg) + " \\\\" + NL;
		}
		
		latexCode +=
		"\\hline" + NL +
		"\\end{tabular}" + NL +
		"\\caption{" + BATCH_DESCRIPTION + ", běhů : " + NUMBER_OF_RUNS + "}" + NL +
		"\\label{tab:" + BATCH_ID + "ResultsStat}" + NL +
		"\\end{table}";

		return latexCode;
	}
	
	protected Map<JobID, Double> readResultsOfJob(String batchID,
			int numberOfRuns, Job job) throws Exception {
		
		String jobId = job.getJobID();
		IProblem problem = job.getProblem();
		
		return FilesystemTool.getTheBestPartResultOfJobForAllRuns(
				batchID, jobId, numberOfRuns, problem);
	}
	
	private String createTableWithResults(Batch batch) throws Exception {

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
			table += "r|";
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

		return latexCode;
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
			
			jobLine += " & " + convertDoubleToString(resultI);
		}

		return jobLine + " \\\\";
	}
	
	private String convertDoubleToString(double value) {
		
		String resultStr = "";
		
		// convert double to format without exponent
		resultStr = String.format("%." + maxLengthOfResult + "f", value);
		
		// cut to given size
		resultStr = resultStr.substring(0, maxLengthOfResult);
		
		// remove zeroes at the end
		if (resultStr.contains(".") || resultStr.contains(",")) {
			resultStr = resultStr.replaceAll("[0]*$", "");
			resultStr = resultStr.replaceAll("\\.$", "");
			resultStr = resultStr.replaceAll("\\,$", "");
		}

		return resultStr;
	}
	
	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchSingleMethodsTSP1083();
//		IInputBatch batchCmp = new BatchHomoMethodsTSP1083();
		
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcTableOfJobRunResults(10);
		p.run(batch);
		
	}
}
