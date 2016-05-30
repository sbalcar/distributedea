package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.distributedea.Configuration;
import org.distributedea.InputConfiguration;
import org.distributedea.agents.FitnessTool;
import org.distributedea.input.Tool;
import org.distributedea.input.batches.BatchHeteroComparingTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;
import org.distributedea.ontology.problem.Problem;

public class PostProcComparing extends PostProcessing {
	
	private String NL = "\n";
	
	public void run(Batch batch) {	
		
		
		String TITLE = batch.getDescription();
		String XLABEL = "čas v sekundách";
		String YLABEL = "hodnota fitnes v kilometrech";
		
		String OUTPUT_FILE = batch.getBatchID() + "Comparing";
		String OUTPUT_PATH = "matlab";
		
		String matlabSourceCode =
		"h = figure" + NL +
		"hold on" + NL +
		"title('" + TITLE + "');" + NL +
		"xlabel('x: " + XLABEL + "', 'FontSize', 10);" + NL +
		"ylabel('y: " + YLABEL + "', 'FontSize', 10);" + NL +
		NL;
		
		List<String> lineTypes = Arrays.asList("-", "--", ":", "-.");
		
		String batchID = batch.getBatchID();
		List<Job> jobWrappers = batch.getJobs();
		for (int i = 0; i < jobWrappers.size(); i++) {
			
			Job jobWrpI = jobWrappers.get(i);
			
			JobID jobIDI = processJobWrapper(jobWrpI, batchID);
			
			String fileNameI = Configuration.getResultFile(jobIDI);
			String lineTypeI = lineTypes.get(i % lineTypes.size());
			
			matlabSourceCode +=
				"M = dlmread('" + "../" + fileNameI + "')" + NL +
				"plot(M,' "+ lineTypeI + "','LineWidth',3);" + NL +
				NL;
		}
		
		String legend = createLegend(batch.exportDescriptions());
		
		matlabSourceCode +=
		"legend(" + legend + ",'Location','best');" + NL +
		NL +
		"legend('show');" + NL +
		NL +
		"hold off" + NL +
		"saveas(h, '" + OUTPUT_FILE + "','jpg');" + NL +
		"exit;";
		
	
		System.out.println(matlabSourceCode);
		
		try(  PrintWriter out = new PrintWriter(OUTPUT_PATH + File.separator + OUTPUT_FILE + ".m")  ){
		    out.println(matlabSourceCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String bashSourceCode = "cd " + OUTPUT_PATH + ";" + NL + 
		"matlab -nodisplay -r " + OUTPUT_FILE;
		
		String bashScriptFileName = OUTPUT_PATH + File.separator + "run.sh";
		try(  PrintWriter out = new PrintWriter(bashScriptFileName)  ){
		    out.println(bashSourceCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (InputConfiguration.runPostProcessing) {
			executeMatlabScript(bashScriptFileName);
		}
	}
	
	public void executeMatlabScript(String bashScriptFileName) {
		
		Runtime rt = Runtime.getRuntime();
		try {
			Process pr0 = rt.exec("chmod +x " + bashScriptFileName);
			pr0.waitFor();
			Process pr1 = rt.exec("./" + bashScriptFileName);
			pr1.waitFor();
			Process pr2 = rt.exec("rm " + bashScriptFileName);
			pr2.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Export OK");
	}

	public JobID processJobWrapper(Job jobWrp, String batchID) {
		
		String jobID = jobWrp.getJobID();
		int numberOfRuns = jobWrp.getNumberOfRuns();
		
		
		Class<?> jobClass = jobWrp.getProblemToSolve();
		
		Problem problem = null;
		try {
			problem = (Problem) jobClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
		}
		
		
		Map<JobID, Double> resultsOfJobsMap = Tool.getResultOfJobForAllRuns(batchID, jobID, numberOfRuns);
		
		return getBestJobID(resultsOfJobsMap, problem);
	}
	
	protected JobID getBestJobID(Map<JobID, Double> resultsOfJobsMap, Problem problem) {
		
		if (resultsOfJobsMap == null || resultsOfJobsMap.isEmpty()) {
			return null;
		}
		
		List<JobID> jobIDs = new ArrayList<>(resultsOfJobsMap.keySet());
		List<Double> fitnessValues = new ArrayList<>(resultsOfJobsMap.values());
		
		JobID bestJobID = jobIDs.get(0);
		Double bestFitnessValue = fitnessValues.get(0);
		
		for (Map.Entry<JobID, Double> entry : resultsOfJobsMap.entrySet()) {
			
			JobID jobIDI = entry.getKey();
			Double fitnessValueI = entry.getValue();
		    
			boolean isBetter = FitnessTool.isFistFitnessBetterThanSecond(
					fitnessValueI, bestFitnessValue, problem);
			
			if (isBetter) {
				bestJobID = jobIDI;
				bestFitnessValue = fitnessValueI;
			}
		}
		
		return bestJobID;
	}
	
	protected String createLegend(List<String> descriptions) {
		
		String legend = "";
		for (int i = 0; i < descriptions.size(); i++) {
			
			legend += "'" + descriptions.get(i) + "'";
			
			if (i < descriptions.size() -1) {
				legend += ",";
			}
		}
		
		return legend;
	}
	
	public static void main(String [] args) {
		
		BatchHeteroComparingTSP batchCmp = new BatchHeteroComparingTSP();
		Batch batch = batchCmp.batch();
		
		PostProcComparing p = new PostProcComparing();
		p.run(batch);
	}
}
