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

public class PostProcJobRunsResultTable extends PostProcessing {

	@Override
	public void run(Batch batch) throws Exception {
		
		String BATCH_ID = batch.getBatchID();
		String BATCH_DESCRIPTION = batch.getDescription();
		int NUMBER_OF_RUNS = batch.getJobs().get(0).getNumberOfRuns();
		
		String NL = "\n";
		List<Job> jobs = batch.getJobs();
		
		String table =
		"\\begin{table} [ht] " + NL +
		"\\centering" + NL;

		
		String latexCode = "|";
		for (int i = 0; i < jobs.size(); i++) {
			latexCode += "c|";
		}
		
		table +=
		"\\begin{tabular}{ " + latexCode + " }" + NL + 
		"\\hline" + NL;
		
		table +=
		"\\textbf{Identifikace job struktur} & \\multicolumn{" + NUMBER_OF_RUNS + "}{|c|}{\\textbf{výsledky běhů}} \\\\" + NL +
		"\\hline\\hline" + NL;
		
		for (Job jobI : jobs) {
			table += processJob(jobI, BATCH_ID) + NL;
		}
		
		table +=
		"\\hline" + NL +
		"\\end{tabular}" + NL +
		"\\caption{" + BATCH_DESCRIPTION + "}" + NL +
		"\\label{tab:" + BATCH_ID + "Results}" + NL +
		"\\end{table}";

		
		System.out.println(table);
		
		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(BATCH_ID);
		String OUTPUT_FILE = BATCH_ID;
		
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
		
		Map<JobID, Double> resultsOfJobsMap = FilesystemTool.getResultOfJobForAllRuns(batchID, jobID, numberOfRuns);
		
		List<Double> resultsOfJobs = new ArrayList<>(resultsOfJobsMap.values());

		String jobLine = job.getDescription();
		for (double resultI : resultsOfJobs) {
			jobLine += " & " + resultI;
		}

		return jobLine + " \\\\";
	}
	
	public static void main(String [] args) throws Exception {
		
//		InputBatch batchCmp = new BatchHeteroComparingTSP();
		IInputBatch batchCmp = new BatchSingleMethodsTSP1083();
//		IInputBatch batchCmp = new BatchHomoMethodsTSP1083();
		
		Batch batch = batchCmp.batch();
		
		PostProcessing p = new PostProcJobRunsResultTable();
		p.run(batch);
	}
}
