package org.distributedea.input.postprocessing.latex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.IPlannerEndCondition;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerEndCondIterationCountRestriction;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.batches.tsp.cities1083.BatchHomoMethodsTSP1083;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;

public class PostProcTableOfJob extends PostProcessing {

	@Override
	public void run(Batch batch) {
		// sorting jobs
		batch.sortJobsByID();

		String BATCH_ID = batch.getBatchID();
		
		String string = "";
		for (Job jobI : batch.getJobs()) {
			String jobStr = processJob(jobI, BATCH_ID);
			string += jobStr;
		}

		String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(BATCH_ID);
		String OUTPUT_FILE = BATCH_ID +
				getClass().getSimpleName().replace("PostProc", "");

		try(  PrintWriter out = new PrintWriter(OUTPUT_PATH + File.separator + OUTPUT_FILE + ".lat")  ){
		    out.println(string);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String processJob(Job job, String BATCH_ID) {
		
		String NL = "\n";
		String JOB_ID = job.getJobID();
		
		String description = job.getDescription();
		int numberOfRuns = job.getNumberOfRuns();
		String problem = job.getProblem().getClass().getSimpleName();
		String problemTools = job.getMethods().exportProblemTools().exportAsString();
		IslandModelConfiguration islandModel = job.getIslandModelConfiguration();
		long broadcastPeriodS = islandModel.getIndividualBroadcastPeriodMS() / 1000;
		long replanPeriodS = islandModel.getReplanPeriodMS() / 1000;
		String isIndividualDistribution = job.getIslandModelConfiguration()
				.isIndividualDistribution() ? "ano" : "ne" ;
		String problemFileName = job.exportDatasetFile().getName();
		String planner = job.getPlanner().getClass().getSimpleName();
		IPlannerEndCondition plannerEndCondition = job.getPlannerEndCondition();
		String plannerEndConditionStr = plannerEndCondition.toString();
		if (plannerEndCondition instanceof PlannerEndCondIterationCountRestriction) {
			PlannerEndCondIterationCountRestriction countRest =
					(PlannerEndCondIterationCountRestriction) plannerEndCondition;
			plannerEndConditionStr = "Omezení na " + countRest
					.exportCountOfReplanningIterations() + " iterací";
		}
		
		
		String jobTableCode =
		"\\begin{table} [hh]" + NL +
		"\\centering" + NL +
		"\\begin{tabular}{|>{\\bfseries}l||l|}" + NL +
		"\\hline" + NL +
		"\\multicolumn{2}{|c|}{Popis Job struktury: " + description + "} \\\\" + NL +
		"\\hline\\hline" + NL +
		" JobID:                & " + JOB_ID + " \\\\" + NL +
		" Problém:              & " + problem + " \\\\" + NL +
		" Distribuce jedinců:   & " + isIndividualDistribution + ", perioda=" + broadcastPeriodS +"sekund \\\\" + NL +
		" Plánovač:             & " + planner + " \\\\" + NL +
		" Ukončovací podmínka:  & " + plannerEndConditionStr + ", perioda=" + replanPeriodS + "sekund \\\\" + NL +
		" Počet běhů:           & " + numberOfRuns + " \\\\" + NL +
		" Dataset:              & " + problemFileName + " \\\\" + NL +
		" Metody:               & " + "" + " \\\\" + NL +
		" ProblemTool nástroj:  & " + problemTools + " \\\\" + NL +
		"\\hline" + NL +
		"\\end{tabular}" + NL +
		"\\caption{Job struktura " + JOB_ID + "}" + NL +
		"\\label{tab:" + BATCH_ID + "Job}" + NL +
		"\\end{table}";

		System.out.println(jobTableCode);
		System.out.println();
		
		//String OUTPUT_PATH = FileNames.getResultDirectoryForMatlab(BATCH_ID);
		//String OUTPUT_FILE1 = BATCH_ID +
		//		getClass().getSimpleName().replace("PostProc", "") +
		//		JOB_ID + "1";
		
		
		String jobAlgorithmTableCode =
		"\\begin{table} [hh]" + NL +
		"\\centering" + NL +
		"\\begin{tabular}{|>{\\bfseries}l||l|}" + NL +
		"\\hline" + NL +
		"\\multicolumn{2}{|c|}{Výpočetní metody: " + description + "} \\\\" + NL +
		"\\hline\\hline" + NL;
		
		InputAgentConfigurations confs =
				job.getMethods().exportInputAgentConfigurations();
		
		for(InputAgentConfiguration confI : confs.getAgentConfigurations()) {
			jobAlgorithmTableCode +=
			confI.exportAgentClass().getSimpleName() + ":        & " + confI.getArguments().exportToString() + " \\\\" + NL;
		}
		jobAlgorithmTableCode +=
		"\\hline" + NL +
		"\\end{tabular}" + NL +
		"\\caption{Job struktura}" + NL +
		"\\label{tab:" + BATCH_ID + "Job}" + NL +
		"\\end{table}";
		
		System.out.println(jobAlgorithmTableCode);
		System.out.println();
		System.out.println();
		
		//String OUTPUT_FILE2 = BATCH_ID +
		//		getClass().getSimpleName().replace("PostProc", "") +
		//		JOB_ID + "2";

		return jobTableCode + NL + NL + jobAlgorithmTableCode + NL + NL;
	}

	public static void main(String [] args) throws IOException {
		
		BatchHomoMethodsTSP1083 batchCmp = new BatchHomoMethodsTSP1083(); 
		Batch batch = batchCmp.batch();
		
		PostProcTableOfJob p = new PostProcTableOfJob();
		p.run(batch);
	}
}
