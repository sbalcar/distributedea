package org.distributedea.input.postprocessing.latex;

import java.io.File;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.tsp.cities1083.BatchHomoMethodsTSP1083;
import org.distributedea.input.postprocessing.PostProcessing;

public class PostProcJobTable extends PostProcessing {

	@Override
	public void run(Batch batch) {
		
		List<Job> jobs = batch.getJobs();
		
		for (Job jobI : jobs) {
			processJob(jobI);
		}
	}
	
	private void processJob(Job job) {
		
		String NL = "\n";
		
		String problemToSolve = job.getProblemDefinition().getClass().getSimpleName();
		String isIndividualDistribution = job.isIndividualDistribution() ? "ano" : "ne" ;
		String problemFileName = job.exportProblemFile().getName();
		String scheduler = job.getPlanner().getClass().getSimpleName();
		int numberOfIteration = 50;
		int numberOfRuns = job.getNumberOfRuns();
		String description = job.getDescription();
		String jobID = job.getJobID();
		File methodsFile = job.exportMethodsFile();
		String methodsFileName = methodsFile.getName().endsWith("methods.xml") ? "všechny" : methodsFile.getName();
		
		String jobTableCode =
		"\\begin{table} [hh]" + NL +
		"\\centering" + NL +
		"\\begin{tabular}{|>{\\bfseries}l||l|}" + NL +
		"\\hline" + NL +
		"\\multicolumn{2}{|c|}{Popis Job struktury: " + description + "} \\\\" + NL +
		"\\hline\\hline" + NL +
		"JobID:                & " + jobID + " \\\\" + NL +
		"Plánovač:             & " + scheduler + " \\\\" + NL +
		"Problém:              & " + problemToSolve + " \\\\" + NL +
		"Distribuce jedinců:   & " + isIndividualDistribution + " \\\\" + NL +
		"Počet přeplánování:   & " + numberOfIteration + " \\\\" + NL +
		"Počet běhů:           & " + numberOfRuns + " \\\\" + NL +
		"Dataset:              & " + problemFileName + " \\\\" + NL +
		"Metody:               & " + methodsFileName + " \\\\" + NL +
		"ProblemTool nástroj:  & 2-OPT \\\\" + NL +
		"\\hline" + NL +
		"\\end{tabular}" + NL +
		"\\caption{Job struktura}" + NL +
		"\\label{tab:template}" + NL +
		"\\end{table}";

		System.out.println(jobTableCode);
		System.out.println();
	}

	public static void main(String [] args) {
		
		BatchHomoMethodsTSP1083 batchCmp = new BatchHomoMethodsTSP1083(); 
		Batch batch = batchCmp.batch();
		
		PostProcJobTable p = new PostProcJobTable();
		p.run(batch);
	}
}
