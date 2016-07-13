package org.distributedea.input.postprocessing.matlab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.distributedea.Configuration;
import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.history.MethodHistory;
import org.distributedea.agents.systemagents.centralmanager.planner.history.MethodInstanceDescription;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;

public class PostProcMonitoring extends PostProcessing {

	String NL = "\n";
	
	@Override
	public void run(Batch batch) {
		
		String batchID = batch.getBatchID();
		
		for (Job jobI : batch.getJobs()) {
			
			processJob(batchID, jobI);
		}
	}

	private void processJob(String batchID, Job job) {
		
		String jobID = job.getJobID();
		
		for (int runNumberI = 0; runNumberI < job.getNumberOfRuns(); runNumberI++) {

			JobID jobIDI = new JobID(batchID, jobID, runNumberI);

			History historyI = null;
			try {
				historyI = History.importXML(jobIDI);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			processRunJob(historyI);
		}
	}
	
	
	private void processRunJob(History history) {
		
		JobID jobID = history.getJobID();
		
		String values = "[";
		String iterations = "[";
		String descriptions = "{";
		for (MethodHistory methodI : history.getMethods()) {
			
			MethodInstanceDescription methodInstanceDescriptionI =
					methodI.getMethodInstanceDescription();
			long numberOfTheBestCreatedIndividualsI =
					methodI.exportNumberOfTheBestCreatedIndividuals();
			long numberOfIterationsI =
					methodI.exportNumberOfIteration();
			
			values += numberOfTheBestCreatedIndividualsI + ",";
			iterations += numberOfIterationsI + ",";
			descriptions += "\'" + methodInstanceDescriptionI.exportInstanceName() + "\'," ;
		}
		values += "]";
		values = values.replaceAll(",]", "]");
		iterations += "]";
		iterations = iterations.replaceAll(",]", "]");   // remove the last ','
		descriptions += "}";
		descriptions = descriptions.replaceAll(",}", "}");  // remove the last ','
		descriptions = descriptions.replaceAll("\\_", "\\\\_");
		
		String RESULT_FILE = Configuration.getResultDirectoryForMatlab(jobID, "pie");
		String MATLAB_FILE = "pie";
		String OUTPUT_PATH = Configuration.getResultDirectoryForMatlab(jobID.getBatchID());
		
		String matlabCode =
			"figure" + NL +
			"ax1 = subplot(3,1,1);" + NL +
			"pie(" + values + ");" + NL +
			"title(ax1,'Přínos metod pro celkový výpočet');" + NL +
			"ax2 = subplot(3,1,2)" + NL +
			"pie(" + iterations + ");" + NL +
			"title(ax2,'Časová kvanta, která dostaly k dispozici');" + NL +
			"labels = " + descriptions + ";" + NL +
			"legend(labels,'Location','southoutside','Orientation','vertical');" + NL +
			"set(gca,'Visible','off');" + NL +
			"saveas(h, '" + RESULT_FILE + "','jpg');" + NL +
			"exit;";
		
		System.out.println(matlabCode);

		try(  PrintWriter out = new PrintWriter(OUTPUT_PATH + File.separator + MATLAB_FILE + ".m")  ){
		    out.println(matlabCode);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String bashSourceCode = "cd " + OUTPUT_PATH + ";" + NL + 
		"matlab -nodisplay -r " + RESULT_FILE;
		
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
}
