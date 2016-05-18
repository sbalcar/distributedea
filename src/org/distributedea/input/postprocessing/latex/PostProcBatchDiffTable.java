package org.distributedea.input.postprocessing.latex;

import java.util.List;

import org.distributedea.input.PostProcessing;
import org.distributedea.input.batches.BatchHomoComparing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

public class PostProcBatchDiffTable extends PostProcessing {

	@Override
	public void run(Batch batch) {
		
		String NL = "\n";
		List<JobWrapper> jobWrps = batch.getJobWrappers();
		
		String table =
		"\\begin{center}" + NL;
		
		String tableSchema = "|";
		for (int i = 0; i < jobWrps.size(); i++) {
			tableSchema += "c|";
		}
		
		table +=
		"\\begin{tabular}{ " + tableSchema + " }" + NL + 
		"\\hline" + NL;
		
		String jobIDStr = "";
		String distributionStr = "";
		String schedulerStr = "";
		for (int i = 0; i < jobWrps.size(); i++) {
			JobWrapper jobWrpI = jobWrps.get(i);
			
			jobIDStr += " " + jobWrpI.getJobID() + " ";
			distributionStr += " " + jobWrpI.isIndividualDistribution() + " ";
			schedulerStr += " " + jobWrpI.getScheduler().getClass().getSimpleName() + " ";
			
			if (i < jobWrps.size() -1) {
				jobIDStr += "&";
				distributionStr += "&";
				schedulerStr += "&";
			}
		}
		table += jobIDStr.trim() + " \\\\" + NL;
		table += distributionStr.trim() + " \\\\" + NL;
		table += schedulerStr + " \\\\" + NL;
		
		table +=
 		"\\hline" + NL +
 		"\\end{tabular}" + NL +
 		"\\end{center}";

		System.out.println(table);
	}

	public static void main(String [] args) {

		BatchHomoComparing batchHomo = new BatchHomoComparing();
		Batch batch = batchHomo.batch();
		
		PostProcBatchDiffTable ps = new PostProcBatchDiffTable();
		ps.run(batch);
	}
}
