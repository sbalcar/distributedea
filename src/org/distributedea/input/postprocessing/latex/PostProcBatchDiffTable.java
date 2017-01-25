package org.distributedea.input.postprocessing.latex;

import java.io.IOException;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.tsp.cities1083.BatchHomoMethodsTSP1083;
import org.distributedea.input.postprocessing.PostProcessing;

public class PostProcBatchDiffTable extends PostProcessing {

	@Override
	public void run(Batch batch) {
		
		String NL = "\n";
		List<Job> jobs = batch.getJobs();
		
		String table =
		"\\begin{center}" + NL;
		
		String tableSchema = "|";
		for (int i = 0; i < jobs.size(); i++) {
			tableSchema += "c|";
		}
		
		table +=
		"\\begin{tabular}{ " + tableSchema + " }" + NL + 
		"\\hline" + NL;
		
		String jobIDStr = "";
		String distributionStr = "";
		String schedulerStr = "";
		for (int i = 0; i < jobs.size(); i++) {
			Job jobWrpI = jobs.get(i);
			
			jobIDStr += " " + jobWrpI.getJobID() + " ";
			distributionStr += " " + jobWrpI.isIndividualDistribution() + " ";
			schedulerStr += " " + jobWrpI.getPlanner().getClass().getSimpleName() + " ";
			
			if (i < jobs.size() -1) {
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

	public static void main(String [] args) throws IOException {

		BatchHomoMethodsTSP1083 batchHomo = new BatchHomoMethodsTSP1083();
		Batch batch = batchHomo.batch();
		
		PostProcBatchDiffTable ps = new PostProcBatchDiffTable();
		ps.run(batch);
	}
}
