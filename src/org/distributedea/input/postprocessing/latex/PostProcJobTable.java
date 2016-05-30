package org.distributedea.input.postprocessing.latex;

import org.distributedea.input.batches.BatchHomoComparingTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;

public class PostProcJobTable extends PostProcessing {

	@Override
	public void run(Batch batch) {
		
		Job jobWrp = batch.getJobs().get(0);
		
		String NL = "\n";
		
		long countOfReplaning = jobWrp.getCountOfReplaning();
		String problemToSolve = jobWrp.getProblemToSolve().getSimpleName();
		String isIndividualDistribution = jobWrp.isIndividualDistribution() ? "ano" : "ne" ;
		String problemFileName = jobWrp.getProblemFileName();
		String scheduler = jobWrp.getScheduler().getClass().getSimpleName();
		String description = jobWrp.getDescription();
		String jobID = jobWrp.getJobID();
		String methodsFileName = jobWrp.getMethodsFileName().endsWith("methods.xml") ? "všechny" : jobWrp.getMethodsFileName();
		
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
		"Pocet preplanovani: & " + countOfReplaning + " \\\\" + NL +
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
