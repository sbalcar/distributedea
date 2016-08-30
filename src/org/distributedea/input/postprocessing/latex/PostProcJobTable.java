package org.distributedea.input.postprocessing.latex;

import java.io.File;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.BatchHomoComparingTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.logging.TrashLogger;

public class PostProcJobTable extends PostProcessing {

	@Override
	public void run(Batch batch) {
		
		Job jobWrp = batch.getJobs().get(0);
		
		String NL = "\n";
		
		String problemToSolve = jobWrp.exportProblemToSolve(new TrashLogger()).getSimpleName();
		String isIndividualDistribution = jobWrp.isIndividualDistribution() ? "ano" : "ne" ;
		String problemFileName = jobWrp.exportProblemFile().getName();
		String scheduler = jobWrp.getPlanner().getClass().getSimpleName();
		String description = jobWrp.getDescription();
		String jobID = jobWrp.getJobID();
		File methodsFile = jobWrp.exportMethodsFile();
		String methodsFileName = methodsFile.getName().endsWith("methods.xml") ? "všechny" : methodsFile.getName();
		
		String jobTableCode =
		"\\begin{table} [hh]" + NL +
		"\\centering" + NL +
		"\\begin{tabular}{|>{\\bfseries}l||l|}" + NL +
		"\\hline" + NL +
		"\\multicolumn{2}{|c|}{Popis Jobu: " + description + "} \\\\" + NL +
		"\\hline\\hline" + NL +
		"JobID:              & " + jobID + " \\\\" + NL +
		"Planovac:           & " + scheduler + " \\\\" + NL +
		"Problem:            & " + problemToSolve + " \\\\" + NL +
		"Distribuce jedincu: & " + isIndividualDistribution + " \\\\" + NL +
		"Dataset:            & " + problemFileName + " \\\\" + NL +
		"Metody:             & " + methodsFileName + " \\\\" + NL +
		"ProblemTool:        & 2-OPT \\\\" + NL +
		"\\hline" + NL +
		"\\end{tabular}" + NL +
		"\\caption{This is a table template}" + NL +
		"\\label{tab:template}" + NL +
		"\\end{table}";

		System.out.println(jobTableCode);
	}

	public static void main(String [] args) {
		
		BatchHomoComparingTSP batchCmp = new BatchHomoComparingTSP(); 
		Batch batch = batchCmp.batch();
		
		PostProcJobTable p = new PostProcJobTable();
		p.run(batch);
	}
}
